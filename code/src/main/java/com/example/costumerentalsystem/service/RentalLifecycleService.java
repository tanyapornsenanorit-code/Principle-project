package com.example.costumerentalsystem.service;

import com.example.costumerentalsystem.dto.request.PaymentRequest;
import com.example.costumerentalsystem.dto.request.ShipmentRequest;
import com.example.costumerentalsystem.dto.response.RentalResponse;

public interface RentalLifecycleService {

    RentalResponse pay(Long rentalId, PaymentRequest request);

    RentalResponse ship(Long rentalId, ShipmentRequest request);

    RentalResponse startUse(Long rentalId);

    RentalResponse returnItem(Long rentalId);

    RentalResponse complete(Long rentalId);

    RentalResponse cancel(Long rentalId);
}