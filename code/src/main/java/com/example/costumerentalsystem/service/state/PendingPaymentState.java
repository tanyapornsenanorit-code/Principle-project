package com.example.costumerentalsystem.service.state;

import org.springframework.stereotype.Component;

import com.example.costumerentalsystem.domain.enums.RentalStatus;

@Component
public class PendingPaymentState extends AbstractRentalState {

    @Override
    public RentalStatus status() {
        return RentalStatus.PENDING_PAYMENT;
    }

    @Override
    public RentalStatus pay() {
        return RentalStatus.PAID;
    }

    @Override
    public RentalStatus cancel() {
        return RentalStatus.CANCELLED;
    }
}
