package com.example.costumerentalsystem.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.domain.enums.RentalStatus;
import com.example.costumerentalsystem.dto.request.RentalCreateRequest;
import com.example.costumerentalsystem.dto.response.RentalQuoteResponse;
import com.example.costumerentalsystem.dto.response.RentalResponse;
import com.example.costumerentalsystem.exception.BadRequestException;
import com.example.costumerentalsystem.exception.ConflictException;
import com.example.costumerentalsystem.exception.ResourceNotFoundException;
import com.example.costumerentalsystem.mapper.RentalMapper;
import com.example.costumerentalsystem.repository.CostumeRepository;
import com.example.costumerentalsystem.repository.RentalRepository;
import com.example.costumerentalsystem.repository.UserRepository;
import com.example.costumerentalsystem.service.RentalService;
import com.example.costumerentalsystem.service.event.RentalStatusChangedEvent;
import com.example.costumerentalsystem.service.pricing.PricingService;
import com.example.costumerentalsystem.service.pricing.RentalPrice;

@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final CostumeRepository costumeRepository;
    private final UserRepository userRepository;
    private final PricingService pricingService;
    private final RentalMapper rentalMapper;
    private final ApplicationEventPublisher eventPublisher;

    public RentalServiceImpl(RentalRepository rentalRepository,
                             CostumeRepository costumeRepository,
                             UserRepository userRepository,
                             PricingService pricingService,
                             RentalMapper rentalMapper,
                             ApplicationEventPublisher eventPublisher) {
        this.rentalRepository = rentalRepository;
        this.costumeRepository = costumeRepository;
        this.userRepository = userRepository;
        this.pricingService = pricingService;
        this.rentalMapper = rentalMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public RentalQuoteResponse quote(Long costumeId, LocalDate startDate, LocalDate endDate) {
        Costume costume = findCostume(costumeId);
        int days = countDays(startDate, endDate);
        RentalPrice price = pricingService.calculate(costume.getPrice(), days);
        return new RentalQuoteResponse(days, price.rentalFee(), price.depositAmount(), price.total(), price.strategyName());
    }

    @Override
    public RentalResponse create(Long userId, RentalCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ผู้ใช้", userId));
        // ล็อกแถวชุดก่อน แล้วค่อยเช็กสถานะกับวันซ้อน
        Costume costume = costumeRepository.findByIdForUpdate(request.costumeId())
                .orElseThrow(() -> new ResourceNotFoundException("ชุด", request.costumeId()));

        if (costume.getStatus() != CostumeStatus.AVAILABLE) {
            throw new ConflictException("ชุดไม่พร้อมสำหรับการเช่า");
        }
        int days = countDays(request.startDate(), request.endDate());
        if (rentalRepository.existsOverlap(costume.getId(), request.startDate(), request.endDate(),
                RentalStatus.activeStatuses())) {
            throw new ConflictException("ชุดนี้ถูกจองในช่วงวันที่เลือกแล้ว");
        }

        RentalPrice price = pricingService.calculate(costume.getPrice(), days);

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCostume(costume);
        rental.setStartDate(request.startDate());
        rental.setEndDate(request.endDate());
        rental.setTotalDays(days);
        rental.setTotalPrice(price.rentalFee());
        rental.setDepositAmount(price.depositAmount());
        rental.setStatus(RentalStatus.PENDING_PAYMENT);
        Rental saved = rentalRepository.save(rental);

        eventPublisher.publishEvent(new RentalStatusChangedEvent(
                saved.getId(), costume.getId(), user.getEmail(), null, RentalStatus.PENDING_PAYMENT));

        return rentalMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RentalResponse getById(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("รายการเช่า", rentalId));
        return rentalMapper.toResponse(rental);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentalResponse> findByUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("ผู้ใช้", userId);
        }
        return rentalRepository.findByUserId(userId, pageable).map(rentalMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentalResponse> findAll(RentalStatus status, Pageable pageable) {
        Page<Rental> page = (status == null)
                ? rentalRepository.findAll(pageable)
                : rentalRepository.findByStatus(status, pageable);
        return page.map(rentalMapper::toResponse);
    }

    private Costume findCostume(Long costumeId) {
        return costumeRepository.findById(costumeId)
                .orElseThrow(() -> new ResourceNotFoundException("ชุด", costumeId));
    }

    // จำนวนวัน = ส่วนต่างของวัน อย่างน้อย 1 วัน (เหมือนระบบเดิม) และห้ามคืนก่อนวันเริ่ม
    private int countDays(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new BadRequestException("วันคืนต้องไม่ก่อนวันเริ่มเช่า");
        }
        return (int) Math.max(1, ChronoUnit.DAYS.between(start, end));
    }
}
 
