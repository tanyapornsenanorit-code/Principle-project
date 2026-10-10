# Design Patterns Analysis

เอกสารนี้อธิบายถึง **Design Patterns** ที่นำมาประยุกต์ใช้ในระบบเช่าชุด (Costume Rental System)

---

## 1. Strategy Pattern (Behavioral Pattern)
* **วัตถุประสงค์:** แยกอัลกอริทึมการคำนวณราคาและค่าปรับออกจาก Business Logic หลัก เพื่อให้สลับเปลี่ยนหรือเพิ่มเงื่อนไขได้ง่าย
* **โครงสร้างการใช้งาน:**
  * `FineStrategy` (Interface): กำหนด Signature method `calculateFine(...)`
  * `StandardFineStrategy` (Concrete Class): คำนวณค่าปรับรายวันตามปกติ
  * `DamageFineStrategy` (Concrete Class): คำนวณค่าปรับกรณีชุดชำรุดเสียหาย

---

## 2. State Pattern (Behavioral Pattern)
* **วัตถุประสงค์:** จัดการสถานะการเช่าชุด (Rental Lifecycle State) ที่เปลี่ยนไปตาม Event ต่างๆ
* **สถานะในระบบ:**
  * `PENDING_PAYMENT` -> `PAID` -> `READY_FOR_PICKUP` -> `RENTED` -> `RETURNED` / `OVERDUE`
* **ประโยชน์:** ช่วยลดการใช้ `if-else` หรือ `switch-case` ซ้อนกันหลายชั้น และควบคุมการเปลี่ยนสถานะให้ถูกต้องตามกฎเกณฑ์

---

## 3. Data Transfer Object (DTO) Pattern (Architectural Pattern)
* **วัตถุประสงค์:** แยกข้อมูลระหว่าง Persistence Model (Entity) ออกจาก Presentation Model (API Request/Response)
* **คลาสที่ใช้:**
  * `CostumeRequestDTO` / `CostumeResponseDTO`
  * `RentalOrderRequestDTO` / `RentalOrderResponseDTO`
* **ประโยชน์:** ป้องกันปัญหา Security (เช่น Over-posting), ลด Data Over-fetching และซ่อนโครงสร้าง Database ไม่ให้ภายนอกเห็น

---

## 4. Repository Pattern (Architectural Pattern)
* **วัตถุประสงค์:** ครอบการจัดการข้อมูลใน Database ให้ทำหน้าที่เหมือน In-memory Collection
* **คลาสที่ใช้:**
  * `CostumeRepository`, `RentalRepository`, `UserRepository` (สืบทอดจาก Spring Data JPA `JpaRepository`)