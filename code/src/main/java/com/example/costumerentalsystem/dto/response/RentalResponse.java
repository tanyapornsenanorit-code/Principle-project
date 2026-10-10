package com.example.costumerentalsystem.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.costumerentalsystem.domain.enums.PaymentStatus;
import com.example.costumerentalsystem.domain.enums.RentalStatus;

public record RentalResponse(
        Long id,
        Long userId,
        String username,
        Long costumeId,
        String costumeName,
        String costumeImageUrl,
        LocalDate startDate,
        LocalDate endDate,
        int totalDays,
        BigDecimal rentalFee,
        BigDecimal depositAmount,
        BigDecimal totalAmount,
        RentalStatus status,
        String statusDisplayName,
        PaymentStatus paymentStatus,
        String courier,
        String trackingNo,
        LocalDateTime createdAt) {
}
