package com.example.costumerentalsystem.service.state;

import com.example.costumerentalsystem.domain.enums.RentalStatus;

/**
 * State: สถานะละคลาส แต่ละคลาสรู้ว่าทำ action ไหนได้และไปสถานะไหนต่อ
 * ถ้า action ไม่อนุญาตจะ throw InvalidRentalTransitionException
 */
public interface RentalState {

    RentalStatus status();

    RentalStatus pay();

    RentalStatus ship();

    RentalStatus startUse();

    RentalStatus returnItem();

    RentalStatus complete();

    RentalStatus cancel();
}
