# 2. Domain Model / Conceptual Class Diagram

## 2.1 Domain Model Diagram

```mermaid
classDiagram
    class User {
        +Long id
        +String username
        +String email
        +String role
    }

    class UserProfile {
        +Long id
        +String fullName
        +String phoneNumber
        +String address
    }

    class Category {
        +Long id
        +String name
        +String description
    }

    class Costume {
        +Long id
        +String name
        +String size
        +Double rentalPricePerDay
        +String status
    }

    class RentalOrder {
        +Long id
        +LocalDate startDate
        +LocalDate endDate
        +Double totalPrice
        +String orderStatus
    }

    class OrderItem {
        +Long id
        +Integer quantity
        +Double price
    }

    class Payment {
        +Long id
        +Double amount
        +LocalDateTime paymentDate
        +String paymentMethod
        +String paymentStatus
    }

    User "1" -- "1" UserProfile : has
    User "1" -- "0..*" RentalOrder : places
    Category "1" -- "0..*" Costume : contains
    RentalOrder "1" -- "1..*" OrderItem : contains
    Costume "1" -- "0..*" OrderItem : referenced_in
    RentalOrder "1" -- "0..1" Payment : requires

```
## 2.2 Conceptual Description

Domain Model นี้แสดง Entity หลักของระบบ Costume Rental System และความสัมพันธ์ระหว่าง Entity ต่างๆ เช่น:

User & UserProfile: ผู้ใช้งาน 1 คนมีโปรไฟล์ 1 โปรไฟล์

Category & Costume: หมวดหมู่ชุด 1 หมวดหมู่ประกอบด้วยชุดหลายชุด

RentalOrder & OrderItem: คำสั่งเช่า 1 คำสั่ง ประกอบด้วยรายการชุดที่เช่า (OrderItem) 1 รายการขึ้นไป

RentalOrder & Payment: คำสั่งเช่าเชื่อมโยงกับการชำระเงิน (Payment)

