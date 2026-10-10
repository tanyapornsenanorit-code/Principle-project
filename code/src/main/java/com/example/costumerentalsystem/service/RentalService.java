package com.example.costumerentalsystem.service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.domain.enums.RentalStatus;
import com.example.costumerentalsystem.repository.CostumeRepository;
import com.example.costumerentalsystem.repository.RentalRepository;

/**
 * ชั่วคราว เพื่อให้ compile กับ Entity ใหม่
 * จะถูกแทนที่ด้วย RentalService (interface) + RentalServiceImpl + PricingStrategy + RentalState
 */
@Service
public class RentalService {

    private static final BigDecimal DEPOSIT_RATE = new BigDecimal("0.5");

    private final RentalRepository rentalRepository;
    private final CostumeRepository costumeRepository;

    public RentalService(RentalRepository rentalRepository, CostumeRepository costumeRepository) {
        this.rentalRepository = rentalRepository;
        this.costumeRepository = costumeRepository;
    }

    @Transactional(readOnly = true)
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Transactional
    public Rental createRental(Rental rental, Long costumeId) {
        Costume costume = costumeRepository.findById(costumeId).orElse(null);

        if (costume == null || costume.getStatus() != CostumeStatus.AVAILABLE) {
            throw new RuntimeException("ชุดไม่พร้อมสำหรับการเช่า");
        }
        if (rental.getStartDate() == null || rental.getEndDate() == null
                || rental.getEndDate().isBefore(rental.getStartDate())) {
            throw new IllegalArgumentException("วันคืนต้องไม่ก่อนวันเริ่มเช่า");
        }

        long days = ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());
        if (days <= 0) {
            days = 1;
        }

        rental.setCostume(costume);
        rental.setTotalDays((int) days);
        rental.setTotalPrice(costume.getPrice().multiply(BigDecimal.valueOf(days)));
        rental.setDepositAmount(costume.getPrice().multiply(DEPOSIT_RATE));
        rental.setStatus(RentalStatus.PENDING_PAYMENT);

        costume.setStatus(CostumeStatus.RESERVED);
        costumeRepository.save(costume);

        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental returnCostume(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental == null) {
            return null;
        }
        rental.setStatus(RentalStatus.RETURNED);

        Costume costume = rental.getCostume();
        if (costume != null) {
            costume.setStatus(CostumeStatus.AVAILABLE);
            costumeRepository.save(costume);
        }
        return rentalRepository.save(rental);
    }
}
