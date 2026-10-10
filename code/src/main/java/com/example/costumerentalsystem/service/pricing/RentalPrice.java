package com.example.costumerentalsystem.service.pricing;

import java.math.BigDecimal;

// ราคาที่คำนวณได้: ค่าเช่า + มัดจำ
public record RentalPrice(BigDecimal rentalFee, BigDecimal depositAmount, String strategyName) {

    public BigDecimal total() {
        return rentalFee.add(depositAmount);
    }
}
