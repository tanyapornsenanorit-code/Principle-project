# 6. Entity Relationship Diagram (ER Diagram)

## 6.1 ER Diagram

```mermaid
erDiagram
    USERS ||--o| USER_PROFILES : "has"
    USERS ||--o{ RENTAL_ORDERS : "places"
    CATEGORIES ||--o{ COSTUMES : "contains"
    COSTUMES ||--o{ ORDER_ITEMS : "referenced_in"
    RENTAL_ORDERS ||--|{ ORDER_ITEMS : "contains"
    RENTAL_ORDERS ||--o| PAYMENTS : "has"

    USERS {
        bigint id PK
        varchar username
        varchar password
        varchar email
        varchar role
        datetime created_at
    }

    USER_PROFILES {
        bigint id PK
        bigint user_id FK
        varchar full_name
        varchar phone_number
        varchar address
    }

    CATEGORIES {
        bigint id PK
        varchar name
        text description
    }

    COSTUMES {
        bigint id PK
        bigint category_id FK
        varchar name
        varchar size
        decimal rental_price_per_day
        varchar status
    }

    RENTAL_ORDERS {
        bigint id PK
        bigint user_id FK
        date start_date
        date end_date
        decimal total_price
        varchar order_status
        datetime created_at
    }

    ORDER_ITEMS {
        bigint id PK
        bigint order_id FK
        bigint costume_id FK
        int quantity
        decimal price
    }

    PAYMENTS {
        bigint id PK
        bigint order_id FK
        decimal amount
        datetime payment_date
        varchar payment_method
        varchar payment_status
    }
```
## 6.2 Database Structure Description
ตารางฐานข้อมูลหลักของระบบประกอบด้วย:

USERS & USER_PROFILES (1:1): ตารางผู้ใช้งานและข้อมูลส่วนตัว

CATEGORIES & COSTUMES (1:N): หมวดหมู่ชุดแต่งกายและรายการชุด

RENTAL_ORDERS & ORDER_ITEMS (1:N): ตารางคำสั่งเช่าชุดและรายการรายละเอียดชุดที่เช่า

RENTAL_ORDERS & PAYMENTS (1:1): ตารางคำสั่งเช่าและประวัติการชำระเงิน