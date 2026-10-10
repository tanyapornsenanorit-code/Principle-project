package com.example.costumerentalsystem.service.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

// strategy แบบลดเป็น % มัดจำคิด 50% ของราคาต่อวัน (เท่าระบบเดิม)
public abstract class DiscountedPricingStrategy implements PricingStrategy {

    private static final BigDecimal DEPOSIT_RATE = new BigDecimal("0.50");

    private final String name;
    private final int minDays;
    private final BigDecimal discountRate;

    protected DiscountedPricingStrategy(String name, int minDays, String discountRate) {
        this.name = name;
        this.minDays = minDays;
        this.discountRate = new BigDecimal(discountRate);
    }

    @Override
    public int minDays() {
        return minDays;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public RentalPrice calculate(BigDecimal pricePerDay, int days) {
        BigDecimal gross = pricePerDay.multiply(BigDecimal.valueOf(days));
        BigDecimal fee = gross.multiply(BigDecimal.ONE.subtract(discountRate)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal deposit = pricePerDay.multiply(DEPOSIT_RATE).setScale(2, RoundingMode.HALF_UP);
        return new RentalPrice(fee, deposit, name);
    }
}
