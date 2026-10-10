package com.example.costumerentalsystem.mapper;

import org.springframework.stereotype.Component;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.entity.Payment;
import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.entity.Shipment;
import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.dto.response.RentalResponse;

@Component
public class RentalMapper {

    public RentalResponse toResponse(Rental rental) {
        User user = rental.getUser();
        Costume costume = rental.getCostume();
        Payment payment = rental.getPayment();
        Shipment shipment = rental.getShipment();

        return new RentalResponse(
                rental.getId(),
                user.getId(),
                user.getUsername(),
                costume.getId(),
                costume.getName(),
                costume.getImageUrl(),
                rental.getStartDate(),
                rental.getEndDate(),
                rental.getTotalDays(),
                rental.getTotalPrice(),
                rental.getDepositAmount(),
                rental.getTotalPrice().add(rental.getDepositAmount()),
                rental.getStatus(),
                rental.getStatus().getDisplayName(),
                payment != null ? payment.getStatus() : null,
                shipment != null ? shipment.getCourier() : null,
                shipment != null ? shipment.getTrackingNo() : null,
                rental.getCreatedAt(),
                payment != null ? payment.getSlipImageUrl() : null);
    }
}
