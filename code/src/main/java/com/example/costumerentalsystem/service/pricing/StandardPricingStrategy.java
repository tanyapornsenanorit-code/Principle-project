package com.example.costumerentalsystem.service.pricing;

import org.springframework.stereotype.Component;

// เช่าปกติ ไม่ลด
@Component
public class StandardPricingStrategy extends DiscountedPricingStrategy {

    public StandardPricingStrategy() {
        super("STANDARD", 1, "0.00");
    }
}
