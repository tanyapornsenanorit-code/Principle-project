package com.example.costumerentalsystem.service.pricing;

import org.springframework.stereotype.Component;

// เช่า 7 วันขึ้นไป ลด 10%
@Component
public class WeeklyPricingStrategy extends DiscountedPricingStrategy {

    public WeeklyPricingStrategy() {
        super("WEEKLY_DISCOUNT", 7, "0.10");
    }
}
