package com.example.costumerentalsystem.service.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.domain.enums.RentalStatus;
import com.example.costumerentalsystem.repository.CostumeRepository;

// เปลี่ยนสถานะชุดตามสถานะใบเช่า ทำใน transaction เดียวกับตอน publish
@Component
public class CostumeStatusListener {

    private final CostumeRepository costumeRepository;

    public CostumeStatusListener(CostumeRepository costumeRepository) {
        this.costumeRepository = costumeRepository;
    }

    @EventListener
    public void onRentalStatusChanged(RentalStatusChangedEvent event) {
        costumeRepository.findById(event.costumeId()).ifPresent(costume -> {
            costume.setStatus(costumeStatusFor(event.to()));
            costumeRepository.save(costume);
        });
    }

    public static CostumeStatus costumeStatusFor(RentalStatus rentalStatus) {
        return switch (rentalStatus) {
            case PENDING_PAYMENT, PAID, SHIPPED -> CostumeStatus.RESERVED;
            case IN_USE -> CostumeStatus.WAITING_RETURN;
            case RETURNED -> CostumeStatus.LAUNDRY;
            case COMPLETED, CANCELLED -> CostumeStatus.AVAILABLE;
        };
    }
}
// Update documentation for Event Listener
