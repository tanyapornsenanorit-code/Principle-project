# Costume Rental System - Rental Lifecycle Module

## Overview
โมดูลสำหรับจัดการวงจรการเช่าชุด (Rental Lifecycle) ตั้งแต่สร้างรายการ ชำระเงิน จัดส่ง คืนชุด จนถึงเสร็จสิ้นรายการ

## Key Components
1. **Entities & Database**: `Payment` และ `Shipment` เชื่อมกับ `Rental` แบบ One-to-One พร้อม Flyway Migration V4
2. **State Machine Validation**: ควบคุมลำดับการเปลี่ยนสถานะด้วย `InvalidRentalTransitionException`
3. **Event Notifications**: ยิง `RentalStatusChangedEvent` แจ้งเตือนผู้ใช้ผ่าน `EmailNotificationListener` แบบ Async (`@Async`)
4. **DTO & Mapping**: สถาปัตยกรรมแยก `dto.request`, `dto.response` และ `PaymentMapper`

## 4. REST API Endpoints
| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/payments` | บันทึกการชำระเงินของรายการเช่า |
| POST | `/api/shipments` | อัปเดตข้อมูลการจัดส่งและ Tracking Number |