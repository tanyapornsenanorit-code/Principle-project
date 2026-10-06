package com.example.costumerentalsystem.service.state;

import org.springframework.stereotype.Component;

import com.example.costumerentalsystem.domain.enums.RentalStatus;

@Component
public class PaidState extends AbstractRentalState {

    @Override
    public RentalStatus status() {
        return RentalStatus.PAID;
    }

    @Override
    public RentalStatus ship() {
        return RentalStatus.SHIPPED;
    }

    @Override
    public RentalStatus cancel() {
        return RentalStatus.CANCELLED;
    }
}
