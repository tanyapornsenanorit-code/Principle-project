package com.example.costumerentalsystem.service.event;

import com.example.costumerentalsystem.domain.enums.RentalStatus;

public record RentalStatusChangedEvent(
        Long rentalId,
        Long costumeId,
        String userEmail,
        RentalStatus fromStatus,
        RentalStatus toStatus
) {}