# 1. Use Case Diagram & Description

## 1.1 Use Case Diagram

```mermaid
graph LR
    Customer((Customer))
    Admin((Admin))

    subgraph Costume Rental System
        UC1[UC-01: Register & Login]
        UC2[UC-02: Search & View Costumes]
        UC3[UC-03: Create Rental Order]
        UC4[UC-04: Process Payment]
        UC5[UC-05: View Rental History]
        UC6[UC-06: Manage Costume Inventory]
        UC7[UC-07: Manage Rental Orders]
        UC8[UC-08: Manage Categories]
    end

    Customer --> UC1
    Customer --> UC2
    Customer --> UC3
    Customer --> UC4
    Customer --> UC5

    Admin --> UC1
    Admin --> UC2
    Admin --> UC6
    Admin --> UC7
    Admin --> UC8
```
## 1.2 Use Case Description

### UC-03: Create Rental Order
- **Actor:** Customer
- **Precondition:** ลูกค้าเข้าสู่ระบบ (Logged in) เรียบร้อยแล้ว
- **Postcondition:** ระบบบันทึกคำสั่งเช่าชุด มีสถานะเป็น `PENDING` และรอการชำระเงิน
- **Main Flow:**
  1. ลูกค้าเลือกชุดแต่งกายที่ต้องการ และระบุวันเริ่มเช่า - วันคืนชุด
  2. ระบบตรวจสอบสถานะความพร้อมของชุดในระบบ (Costume Availability)
  3. ระบบคำนวณราคารวม (Total Price)
  4. ลูกค้ายืนยันการเช่า
  5. ระบบสร้าง `RentalOrder` และเปลี่ยนสถานะชุดชั่วคราว