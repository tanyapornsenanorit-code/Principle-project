# SOLID Principles Analysis

เอกสารนี้วิเคราะห์การประยุกต์ใช้หลักการ **SOLID** ในระบบเช่าชุด (Costume Rental System) เพื่อให้โค้ดมีความยืดหยุ่น ดูแลรักษาง่าย และรองรับการขยายตัวในอนาคต

---

## 1. Single Responsibility Principle (SRP)
> **หลักการ:** แต่ละคลาสควรมีหน้าที่และความรับผิดชอบเพียงอย่างเดียว

* **การนำมาใช้ในโปรเจกต์:**
  * **Controller Layer (`CostumeController`, `RentalController`):** รับผิดชอบเฉพาะการจัดการ HTTP Request, Validation และการคืนค่า HTTP Response เท่านั้น
  * **Service Layer (`RentalService`, `UserService`):** รับผิดชอบ Business Logic เช่น คำนวณวันเช่า ตรวจสอบสถานะการพร้อมใช้งานของชุด
  * **FineCalculationService:** แยกคลาสสำหรับคำนวณค่าปรับการคืนชุดล่าช้าหรือชำรุดโดยเฉพาะ ไม่ปะปนกับกระบวนการเช่าปกติ
  * **Repository Layer (`CostumeRepository`, `RentalRepository`):** รับผิดชอบเฉพาะการติดต่อและจัดการข้อมูลใน Database ผ่าน Spring Data JPA

---

## 2. Open/Closed Principle (OCP)
> **หลักการ:** ซอฟต์แวร์ควรเปิดรับการขยาย (Open for extension) แต่ปิดสำหรับการแก้ไข (Closed for modification)

* **การนำมาใช้ในโปรเจกต์:**
  * การคำนวณค่าปรับใช้ Interface **`FineStrategy`** หรือ **`PricingStrategy`** ทำให้เมื่อต้องการเพิ่มสูตรคำนวณค่าปรับแบบใหม่ (เช่น ค่าปรับตามประเภทชุด หรือช่วงเทศกาล) สามารถสร้าง Class ใหม่ที่ implements Interface นี้ได้ทันที โดยไม่ต้องแก้ไขโค้ดเดิมใน Service หลัก

---

## 3. Liskov Substitution Principle (LSP)
> **หลักการ:** คลาสลูก (Subclass) ต้องสามารถใช้งานแทนคลาสแม่ (Superclass / Interface) ได้โดยไม่ทำให้การทำงานของระบบผิดเพี้ยน

* **การนำมาใช้ในโปรเจกต์:**
  * การใช้ Spring Data JPA Interface เช่น `JpaRepository<Costume, Long>` ซึ่งคลาสที่ Spring Gen มาให้สามารถทำงานแทน Interface ได้สมบูรณ์
  * ชนิดข้อมูลและ Exception ที่ส่งกลับจาก Service Implementation ปฏิบัติตามสัญญา (Contract) ที่กำหนดไว้ใน Interface เสมอ

---

## 4. Interface Segregation Principle (ISP)
> **หลักการ:** ไม่ควรบังคับให้คลาสใดๆ สืบทอด Interface ที่มี method ที่คลาสนั้นไม่ได้ใช้งาน

* **การนำมาใช้ในโปรเจกต์:**
  * แยก Repository และ Service ออกเป็นโมดูลย่อยๆ ตาม Domain Context เช่น `UserRepository`, `CostumeRepository`, `RentalRepository` แทนที่จะสร้าง Repository ขนาดใหญ่ที่รวมทุกอย่างไว้ในที่เดียว
  * แยก Data Transfer Objects (DTO) ตามการใช้งาน เช่น `CostumeCreateDTO`, `CostumeResponseDTO` เพื่อไม่ให้ Client รับส่ง Field ที่ไม่จำเป็น

---

## 5. Dependency Inversion Principle (DIP)
> **หลักการ:** คลาสระดับสูง (High-level) ไม่ควรยึดติดกับคลาสระดับต่ำ (Low-level) แต่ควรอ้างอิงผ่าน Abstraction (Interface/Class)

* **การนำมาใช้ในโปรเจกต์:**
  * **Constructor Injection:** เปลี่ยนจากการใช้ `@Autowired` บน Field มาใช้ Constructor Injection ทุกจุด
  * **Controller -> Service:** Controller เรียกใช้งานผ่าน Service Interface/Component แทนที่จะเรียก Repository หรือ Database โดยตรง