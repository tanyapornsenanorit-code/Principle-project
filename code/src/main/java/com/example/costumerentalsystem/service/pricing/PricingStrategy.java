package com.example.costumerentalsystem.service.pricing;

import java.math.BigDecimal;

/**
 * Strategy: แต่ละคลาสคือวิธีคิดราคาหนึ่งแบบ
 * อยากเพิ่มโปรใหม่ก็เพิ่มคลาสที่ implements ตัวนี้ ไม่ต้องไปแก้ if-else (OCP)
 */
public interface PricingStrategy {

    // เช่ากี่วันขึ้นไปถึงจะใช้แบบนี้ (ระบบเลือกตัวที่ minDays มากสุดที่ยังเข้าเงื่อนไข)
    int minDays();

    String name();

    RentalPrice calculate(BigDecimal pricePerDay, int days);
}
