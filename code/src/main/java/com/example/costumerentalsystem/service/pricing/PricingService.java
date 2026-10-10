package com.example.costumerentalsystem.service.pricing;

import java.math.BigDecimal;

public interface PricingService {

    RentalPrice calculate(BigDecimal pricePerDay, int days);
}
