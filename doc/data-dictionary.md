# Data Dictionary (พจนานุกรมข้อมูล)

เอกสารระบุโครงสร้างตารางและฟิลด์ข้อมูลในระบบเช่าชุด (Costume Rental System)

---

## 1. Table: `users` (ตารางผู้ใช้งานระบบ)

| Column Name | Data Type | Constraint | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | รหัสอ้างอิงผู้ใช้ |
| `username` | VARCHAR(50) | NOT NULL, UNIQUE | ชื่อบัญชีผู้ใช้งาน |
| `password` | VARCHAR(255) | NOT NULL | รหัสผ่าน (Encrypted) |
| `email` | VARCHAR(100) | NOT NULL, UNIQUE | อีเมลผู้ใช้งาน |
| `full_name` | VARCHAR(100) | NOT NULL | ชื่อ-นามสกุล |
| `role` | VARCHAR(20) | NOT NULL | สิทธิ์การใช้งาน (`CUSTOMER`, `ADMIN`) |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | วันเวลาที่สร้างบัญชี |

---

## 2. Table: `costumes` (ตารางชุดเช่า)

| Column Name | Data Type | Constraint | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | รหัสอ้างอิงชุดเช่า |
| `code` | VARCHAR(30) | NOT NULL, UNIQUE | รหัสสินค้า/ชุด |
| `name` | VARCHAR(100) | NOT NULL | ชื่อชุดเช่า |
| `category` | VARCHAR(50) | NOT NULL | หมวดหมู่ชุด (เช่น ชุดไทย, คอสเพลย์) |
| `size` | VARCHAR(10) | NOT NULL | ขนาดชุด (`S`, `M`, `L`, `XL`) |
| `rental_price_per_day` | DECIMAL(10,2) | NOT NULL | ราคาค่าเช่าต่อวัน |
| `deposit_price` | DECIMAL(10,2) | NOT NULL | ค่ามัดจำชุด |
| `status` | VARCHAR(20) | NOT NULL | สถานะชุด (`AVAILABLE`, `RENTED`, `MAINTENANCE`) |

---

## 3. Table: `rental_orders` (ตารางรายการเช่าชุด)

| Column Name | Data Type | Constraint | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | รหัสอ้างอิงใบเช่า |
| `user_id` | BIGINT | FOREIGN KEY (`users.id`) | รหัสผู้เช่าชุด |
| `costume_id` | BIGINT | FOREIGN KEY (`costumes.id`) | รหัสชุดที่เช่า |
| `rental_start_date` | DATE | NOT NULL | วันที่เริ่มต้นเช่า |
| `rental_end_date` | DATE | NOT NULL | วันที่กำหนดคืน |
| `actual_return_date` | DATE | NULL | วันที่คืนชุดจริง |
| `total_price` | DECIMAL(10,2) | NOT NULL | ราคารวมค่าเช่า |
| `fine_amount` | DECIMAL(10,2) | DEFAULT 0.00 | ค่าปรับ (ถ้ามี) |
| `status` | VARCHAR(30) | NOT NULL | สถานะรายการเช่า (`PENDING_PAYMENT`, `PAID`, `RENTED`, `RETURNED`, `OVERDUE`) |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | วันเวลาที่ทำรายการ |