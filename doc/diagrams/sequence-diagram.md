# 4. Sequence Diagram

## 4.1 Sequence Diagram: Create Rental Order & Payment

```mermaid
sequenceDiagram
    autonumber
    actor Customer
    participant Controller as RentalOrderController
    participant Service as RentalOrderServiceImpl
    participant Strategy as PaymentStrategy
    participant Repo as RentalOrderRepository

    Customer->>Controller: createOrder(OrderRequestDTO)
    activate Controller
    Controller->>Service: createOrder(dto)
    activate Service
    
    Service->>Repo: checkAvailability(costumeId, dates)
    activate Repo
    Repo-->>Service: isAvailable (true)
    deactivate Repo

    Service->>Service: calculateTotalPrice(days, price)
    Service->>Repo: save(RentalOrder)
    activate Repo
    Repo-->>Service: RentalOrder (Status: PENDING)
    deactivate Repo

    Service-->>Controller: Order Created Response
    deactivate Service
    Controller-->>Customer: Display Order Summary & Payment Options
    deactivate Controller

    Customer->>Controller: payOrder(orderId, paymentDetails)
    activate Controller
    Controller->>Service: processPayment(orderId, strategy)
    activate Service

    Service->>Strategy: pay(amount)
    activate Strategy
    Strategy-->>Service: Payment Success (true)
    deactivate Strategy

    Service->>Repo: updateStatus(orderId, ACTIVE)
    activate Repo
    Repo-->>Service: Order Updated
    deactivate Repo

    Service-->>Controller: Payment Success Response
    deactivate Service
    Controller-->>Customer: Show Order Confirmation
    deactivate Controller
```

## 4.2 Description

ลำดับขั้นตอนการทำงานของการเช่าชุดและการชำระเงิน (Create Rental Order & Process Payment):

การสร้างคำสั่งเช่า: ลูกค้าส่งคำร้องขอสร้างคำสั่งเช่าผ่าน RentalOrderController

การตรวจสอบความพร้อม: ระบบตรวจสอบความพร้อมของชุดในฐานข้อมูล หากว่าง จะคำนวณราคารวมและบันทึกคำสั่งเช่าสถานะ PENDING

การชำระเงิน: ลูกค้าเลือกวิธีการชำระเงิน และระบบประมวลผลผ่าน PaymentStrategy (Strategy Pattern)

การยืนยัน: เมื่อชำระเงินสำเร็จ ระบบจะอัปเดตสถานะการเช่าเป็น ACTIVE และส่งผลยืนยันกลับไปยังลูกค้า