package com.example.costumerentalsystem.service.pricing;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

// Spring ส่ง PricingStrategy ทุกตัวเข้ามาใน list แล้วเลือกตัวที่ minDays สูงสุดที่ยังใช้ได้
@Component
public class PricingServiceImpl implements PricingService {

    private final List<PricingStrategy> strategies;

    public PricingServiceImpl(List<PricingStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public RentalPrice calculate(BigDecimal pricePerDay, int days) {
        return strategies.stream()
                .filter(strategy -> strategy.minDays() <= days)
                .max(Comparator.comparingInt(PricingStrategy::minDays))
                .orElseThrow(() -> new IllegalStateException("ไม่มี PricingStrategy ที่รองรับ " + days + " วัน"))
                .calculate(pricePerDay, days);
    }
}
