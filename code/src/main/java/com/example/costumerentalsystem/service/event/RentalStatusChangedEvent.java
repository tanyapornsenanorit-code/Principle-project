package com.example.costumerentalsystem.service.event;

import com.example.costumerentalsystem.domain.enums.RentalStatus;

/**
 * Observer: ประกาศว่าสถานะใบเช่าเปลี่ยนแล้ว ใครอยากรู้ก็ฟังได้
 * ตัวที่ publish ไม่ต้องรู้ว่ามีใครฟังบ้าง (ไม่ต้องผูกกับเรื่องอีเมลหรือสถานะชุด)
 * from เป็น null ตอนสร้างใบเช่าใหม่
 */
public record RentalStatusChangedEvent(
        Long rentalId,
        Long costumeId,
        String userEmail,
        RentalStatus from,
        RentalStatus to) {
}
