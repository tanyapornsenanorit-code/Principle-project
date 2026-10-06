package com.example.costumerentalsystem.service.state;

import org.springframework.stereotype.Component;

import com.example.costumerentalsystem.domain.enums.RentalStatus;

@Component
public class ReturnedState extends AbstractRentalState {

    @Override
    public RentalStatus status() {
        return RentalStatus.RETURNED;
    }

    @Override
    public RentalStatus complete() {
        return RentalStatus.COMPLETED;
    }
}
