# 7. Component Diagram

## 7.1 Component Diagram

```mermaid
graph TD
    subgraph Frontend Client
        UI[Web User Interface / Single Page App]
    end

    subgraph Backend Application Server
        subgraph Controller Layer
            AuthCtrl[AuthController]
            CostumeCtrl[CostumeController]
            OrderCtrl[RentalOrderController]
        end

        subgraph Service Layer
            AuthService[AuthService]
            CostumeService[CostumeService]
            OrderService[RentalOrderService]
            PaymentService[PaymentService]
        end

        subgraph Repository Layer
            UserRepo[UserRepository]
            CostumeRepo[CostumeRepository]
            OrderRepo[RentalOrderRepository]
        end
    end

    subgraph External Infrastructure
        DB[(Relational Database)]
        PaymentAPI[External Payment Gateway API]
    end

    UI -->|HTTP / REST JSON| Controller Layer
    Controller Layer --> Service Layer
    Service Layer --> Repository Layer
    Repository Layer -->|JPA / JDBC| DB
    PaymentService -->|HTTPS API Call| PaymentAPI
```
## 7.2 Component Description
Component Diagram แสดงโครงสร้างส่วนประกอบของระบบแบบ Layered Architecture:

Frontend Client: ส่วนแสดงผลเว็บแอปพลิเคชันสำหรับโต้ตอบกับผู้ใช้งาน

Controller Layer: รับ Request จาก Frontend และส่งต่อการประมวลผลไปยัง Service Layer

Service Layer: ส่วนประมวลผล Logic ของระบบ เช่น การคำนวณราคาเช่า การตรวจสอบสถานะชุด และการจัดการชำระเงิน

Repository Layer: ส่วนสื่อสารและจัดการข้อมูลระหว่างแอปพลิเคชันกับฐานข้อมูล

External Infrastructure: ฐานข้อมูลหลัก (Database) และบริการระบบชำระเงินภายนอก (Payment Gateway API)