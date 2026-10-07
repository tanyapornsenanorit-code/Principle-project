package com.example.costumerentalsystem.service.event;

import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.domain.enums.RentalStatus;
import com.example.costumerentalsystem.repository.CostumeRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CostumeStatusListener {

    private final CostumeRepository costumeRepository;

    public CostumeStatusListener(CostumeRepository costumeRepository) {
        this.costumeRepository = costumeRepository;
    }

    @EventListener
    public void handleRentalStatusChanged(RentalStatusChangedEvent event) {
        if (event.costumeId() == null) {
            return;
        }

        costumeRepository.findById(event.costumeId()).ifPresent(costume -> {
            if (event.toStatus() == RentalStatus.COMPLETED || event.toStatus() == RentalStatus.CANCELLED) {
                costume.setStatus(CostumeStatus.AVAILABLE);
            } else if (event.toStatus() == RentalStatus.SHIPPED || event.toStatus() == RentalStatus.IN_USE) {
                costume.setStatus(CostumeStatus.UNAVAILABLE);
            }
            costumeRepository.save(costume);
        });
    }
}