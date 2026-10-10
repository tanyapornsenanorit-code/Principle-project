package com.example.costumerentalsystem.service.state;

import org.springframework.stereotype.Component;

import com.example.costumerentalsystem.domain.enums.RentalStatus;

@Component
public class ShippedState extends AbstractRentalState {

    @Override
    public RentalStatus status() {
        return RentalStatus.SHIPPED;
    }

    @Override
    public RentalStatus startUse() {
        return RentalStatus.IN_USE;
    }
}
// State transition listener
