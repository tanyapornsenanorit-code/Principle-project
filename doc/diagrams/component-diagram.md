# 7. Component Diagram

## 7.1 Component Diagram

```mermaid
graph TD
    subgraph Frontend_Client ["Frontend Client"]
        UI[Web User Interface / Single Page App]
    end

    subgraph Backend_Server ["Backend Application Server"]
        subgraph Controller_Layer ["Controller Layer"]
            AuthCtrl[AuthController]
            CostumeCtrl[CostumeController]
            OrderCtrl[RentalOrderController]
        end

        subgraph Service_Layer ["Service Layer"]
            AuthService[AuthService]
            CostumeService[CostumeService]
            OrderService[RentalOrderService]
            PaymentService[PaymentService]
        end

        subgraph Repository_Layer ["Repository Layer"]
            UserRepo[UserRepository]
            CostumeRepo[CostumeRepository]
            OrderRepo[RentalOrderRepository]
        end
    end

    subgraph External_Infra ["External Infrastructure"]
        DB[(Relational Database)]
        PaymentAPI[External Payment Gateway API]
    end

    UI -->|HTTP / REST API| AuthCtrl
    UI -->|HTTP / REST API| CostumeCtrl
    UI -->|HTTP / REST API| OrderCtrl

    AuthCtrl --> AuthService
    CostumeCtrl --> CostumeService
    OrderCtrl --> OrderService

    AuthService --> UserRepo
    CostumeService --> CostumeRepo
    OrderService --> OrderRepo
    OrderService --> PaymentService

    UserRepo -->|JPA / JDBC| DB
    CostumeRepo -->|JPA / JDBC| DB
    OrderRepo -->|JPA / JDBC| DB
    PaymentService -->|HTTPS API Call| PaymentAPI
```
## 7.2 Component Description

Component Diagram แสดงโครงสร้างส่วนประกอบของระบบแบบ Layered Architecture:

Frontend Client: ส่วนแสดงผลเว็บแอปพลิเคชันสำหรับโต้ตอบกับผู้ใช้งาน

Controller Layer: รับ Request จาก Frontend และส่งต่อการประมวลผลไปยัง Service Layer

Service Layer: ส่วนประมวลผล Logic ของระบบ เช่น การคำนวณราคาเช่า การตรวจสอบสถานะชุด และการจัดการชำระเงิน

Repository Layer: ส่วนสื่อสารและจัดการข้อมูลระหว่างแอปพลิเคชันกับฐานข้อมูล

External Infrastructure: ฐานข้อมูลหลัก (Database) และบริการระบบชำระเงินภายนอก (Payment Gateway API)