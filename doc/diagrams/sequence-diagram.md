# 4. Sequence Diagrams

## 4.1 Rental & Payment Process (การเช่าชุดและการชำระเงิน)

```mermaid
sequenceDiagram
    autonumber
    actor Customer
    participant WebUI as Frontend / Web UI
    participant RentalCtrl as RentalController
    participant RentalSvc as RentalService
    participant DB as Database
    participant PaymentGW as Payment Gateway

    Customer->>WebUI: Select costume & rent dates
    WebUI->>RentalCtrl: POST /api/v1/rentals (RentalRequest)
    RentalCtrl->>RentalSvc: createRental(customerId, costumeId, dates)
    RentalSvc->>DB: Check costume availability
    DB-->>RentalSvc: Available
    RentalSvc->>DB: Save RentalOrder (Status: PendingPayment)
    DB-->>RentalSvc: Saved Order
    RentalSvc-->>RentalCtrl: RentalOrderDTO
    RentalCtrl-->>WebUI: 201 Created (Order details)

    Customer->>WebUI: Pay for rental
    WebUI->>PaymentGW: Process payment
    PaymentGW-->>WebUI: Payment Success
    WebUI->>RentalCtrl: PUT /api/v1/rentals/{id}/pay
    RentalCtrl->>RentalSvc: confirmPayment(orderId)
    RentalSvc->>DB: Update status to PAID
    DB-->>RentalSvc: Updated
    RentalSvc-->>RentalCtrl: Success
    RentalCtrl-->>WebUI: 200 OK (Payment Confirmed)
```

## 4.2 Costume Return & Fine Calculation Process (การคืนชุดและการคำนวณค่าปรับ)
```mermaid
sequenceDiagram
    autonumber
    actor Admin
    participant WebUI as Frontend / Web UI
    participant ReturnCtrl as RentalController
    participant ReturnSvc as ReturnService
    participant FineSvc as FineCalculationService
    participant DB as Database

    Admin->>WebUI: Input rental order ID & return condition
    WebUI->>ReturnCtrl: POST /api/v1/rentals/{id}/return
    ReturnCtrl->>ReturnSvc: processReturn(orderId, returnDate, condition)
    ReturnSvc->>FineSvc: calculateFine(orderId, actualReturnDate)
    
    alt Return is Overdue or Damaged
        FineSvc-->>ReturnSvc: Fine Amount Calculated
        ReturnSvc->>DB: Update Order (Status: RETURNED_WITH_FINE, FineAmount)
    else Returned On Time & Good Condition
        FineSvc-->>ReturnSvc: Fine = 0
        ReturnSvc->>DB: Update Order (Status: RETURNED)
    end

    ReturnSvc->>DB: Update Costume Status to AVAILABLE
    DB-->>ReturnSvc: Success
    ReturnSvc-->>ReturnCtrl: ReturnSummaryDTO
    ReturnCtrl-->>WebUI: 200 OK (Return Summary & Fine details)
```

## 4.3 Costume Management Process by Admin (การจัดการข้อมูลชุดโดยผู้ดูแลระบบ)

```mermaid
sequenceDiagram
    autonumber
    actor Admin
    participant WebUI as Frontend / Web UI
    participant CostumeCtrl as CostumeController
    participant CostumeSvc as CostumeService
    participant DB as Database

    Admin->>WebUI: Fill new costume form & Submit
    WebUI->>CostumeCtrl: POST /api/v1/costumes (CostumeDTO)
    CostumeCtrl->>CostumeSvc: createCostume(CostumeDTO)
    CostumeSvc->>DB: Save new costume record
    DB-->>CostumeSvc: Saved Costume Entity
    CostumeSvc-->>CostumeCtrl: CostumeResponseDTO
    CostumeCtrl-->>WebUI: 201 Created
    WebUI-->>Admin: Display success message & updated costume list
```