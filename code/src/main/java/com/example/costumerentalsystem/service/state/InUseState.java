package com.example.costumerentalsystem.service.state;

import org.springframework.stereotype.Component;

import com.example.costumerentalsystem.domain.enums.RentalStatus;

@Component
public class InUseState extends AbstractRentalState {

    @Override
    public RentalStatus status() {
        return RentalStatus.IN_USE;
    }

    @Override
    public RentalStatus returnItem() {
        return RentalStatus.RETURNED;
    }
}
