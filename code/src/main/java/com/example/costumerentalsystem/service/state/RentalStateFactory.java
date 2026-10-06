package com.example.costumerentalsystem.service.state;

import com.example.costumerentalsystem.domain.enums.RentalStatus;

public interface RentalStateFactory {

    RentalState stateOf(RentalStatus status);
}
