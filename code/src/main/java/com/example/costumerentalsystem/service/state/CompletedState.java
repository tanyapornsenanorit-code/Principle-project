package com.example.costumerentalsystem.service.state;

import org.springframework.stereotype.Component;

import com.example.costumerentalsystem.domain.enums.RentalStatus;

@Component
public class CompletedState extends AbstractRentalState {

    @Override
    public RentalStatus status() {
        return RentalStatus.COMPLETED;
    }
}
