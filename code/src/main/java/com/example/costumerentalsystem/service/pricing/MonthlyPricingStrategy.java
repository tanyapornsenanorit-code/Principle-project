package com.example.costumerentalsystem.service.pricing;

import org.springframework.stereotype.Component;

// เช่า 30 วันขึ้นไป ลด 20%
@Component
public class MonthlyPricingStrategy extends DiscountedPricingStrategy {

    public MonthlyPricingStrategy() {
        super("MONTHLY_DISCOUNT", 30, "0.20");
    }
}
