package com.example.costumerentalsystem.service;

import com.example.costumerentalsystem.dto.request.PaymentRequest;
import com.example.costumerentalsystem.dto.request.ShipmentRequest;
import com.example.costumerentalsystem.dto.response.RentalResponse;

public interface RentalLifecycleService {

    RentalResponse submitPayment(Long rentalId, PaymentRequest request);

    RentalResponse verifyPayment(Long rentalId);

    RentalResponse rejectPayment(Long rentalId);

    /**
     * Compatibility alias. Submitting a payment does not approve it.
     */
    @Deprecated
    default RentalResponse pay(Long rentalId, PaymentRequest request) {
        return submitPayment(rentalId, request);
    }

    RentalResponse ship(Long rentalId, ShipmentRequest request);

    RentalResponse startUse(Long rentalId);

    RentalResponse returnItem(Long rentalId);

    RentalResponse complete(Long rentalId);

    RentalResponse cancel(Long rentalId);
}