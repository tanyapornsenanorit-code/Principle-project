# 9. State Diagram

## 9.1 Rental Order State Diagram

```mermaid
stateDiagram-v2
    [*] --> DRAFT : Create Order

    state DRAFT {
        [*] --> PendingPayment
        PendingPayment --> PAID : Payment Success
        PendingPayment --> CANCELLED : Payment Failed / Timeout
    }

    PAID --> PREPARING : Admin Confirm Order
    PREPARING --> READY_FOR_PICKUP : Costume Packed

    READY_FOR_PICKUP --> RENTED : Customer Picked Up
    
    state RENTED {
        [*] --> InUse
        InUse --> PendingReturn : Return Date Reached
    }

    RENTED --> RETURNED : Returned On Time
    RENTED --> OVERDUE : Late Return

    OVERDUE --> RETURNED_WITH_FINE : Late Fee Paid & Returned

    CANCELLED --> [*]
    RETURNED --> COMPLETED
    RETURNED_WITH_FINE --> COMPLETED

    COMPLETED --> [*]
```
## 9.2 State Description
State Diagram แสดงวงจรชีวิตและสถานะของรายการเช่าชุด (Rental Order Lifecycle):

DRAFT / PendingPayment: สร้างรายการเช่าและรอการชำระเงิน

PAID: ชำระเงินเรียบร้อย รอเจ้าหน้าที่จัดเตรียมชุด

PREPARING / READY_FOR_PICKUP: เตรียมชุดเสร็จสิ้น พร้อมให้ลูกค้ารับชุด

RENTED / InUse: ลูกค้าอยู่ระหว่างการนำชุดไปใช้งาน

RETURNED: คืนชุดตรงเวลา สถานะเสร็จสมบูรณ์ (COMPLETED)

OVERDUE / RETURNED_WITH_FINE: คืนชุดเกินกำหนด คิดค่าปรับ และเปลี่ยนสถานะเป็นเสร็จสมบูรณ์เมื่อชำระค่าปรับครบถ้วน