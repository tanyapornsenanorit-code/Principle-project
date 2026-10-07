package com.example.costumerentalsystem.mapper;

import com.example.costumerentalsystem.domain.entity.Payment;
import com.example.costumerentalsystem.dto.request.PaymentRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequest request) {
        if (request == null) {
            return null;
        }
        Payment payment = new Payment();
        if (request.method() != null) {
            payment.setPaymentMethod(request.method().name());
        }
        payment.setSlipImageUrl(request.slipImageUrl());
        payment.setPaymentDate(LocalDateTime.now());
        return payment;
    }
}