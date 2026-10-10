# 🎭 Costume Rental System (ระบบบริหารจัดการเช่าชุดออนไลน์)

ระบบบริหารจัดการการเช่าชุดออนไลน์ พัฒนาด้วย **Java Spring Boot** และ **Thymeleaf** ช่วยให้ผู้ใช้งานสามารถค้นหา เลือกเช่าชุด ติดตามสถานะการส่งพัสดุ และจัดการข้อมูลส่วนตัวได้สะดวก พร้อมระบบผู้ดูแลระบบ (Admin) สำหรับบริหารจัดการชุดและรายการเช่าอย่างครบวงจร

## 👥 สมาชิกกลุ่ม

| ลำดับ | ชื่อ-นามสกุล | รหัสนักศึกษา | Section | Branch | หน้าที่รับผิดชอบ |
| :---: | :--- | :---: | :---: | :---: | :--- |
| 1 | นางสาวศศิวิตรา วงศ์รุ่งอรุณเลิศ | 673380602-6 | 3 | sasiwitraw_673380602-6_03 | พัฒนา Backend และ REST API โดยรับผิดชอบ Service Layer, Rental System, Exception Handling, Swagger/OpenAPI และ Design Patterns พร้อมเชื่อม Controller กับ Service |
| 2 | นางสาวธันยพร เสนาโนฤทธิ์ | 673380587-6 | 3 | tanyaporn_6733805876_03 | จัดโครงสร้างโปรเจกต์, ตั้งค่าฐานข้อมูล PostgreSQL & Flyway (V1-V3), สร้าง Entity/DTO/Mapper/Exception, ทำระบบแจ้งชำระเงิน และตกแต่งแก้ไขหน้าเว็บ HTML ให้สวยงาม (Thymeleaf) |
| 3 | นางสาวทัดพิชา วะสาร | 673380584-2 | 3 | Thadpeecha_673380548-2_03 | ออกแบบและเชื่อมต่อฐานข้อมูล (Database Design & Backend Integration) สร้าง Data Entity, พัฒนา Business Logic (Service/JPA) และตั้งค่า Spring Security |

---
link deploy: https://principle-project.onrender.com
---

## 🌟 ฟีเจอร์หลักของระบบ (Key Features)

### 1. 🔐 ระบบยืนยันตัวตนและความปลอดภัย (Authentication & Security)
* **เข้าสู่ระบบ / สมัครสมาชิก:** รองรับการยืนยันตัวตนด้วย Spring Security
* **การแบ่งสิทธิ์ผู้ใช้งาน (Role-based Access Control):** 
  * **Guest:** เข้าดูรายการชุดเช่าและรายละเอียดได้
  * **User:** ดำเนินการเช่าชุด ติดตามออเดอร์ และแก้ไขข้อมูลส่วนตัว
  * **Admin:** เข้าถึงระบบจัดการหลังบ้าน (Admin Dashboard)
* **ระบบรีเซ็ตรหัสผ่าน (Forgot & Reset Password):**
  * ขอรหัสยืนยัน OTP ผ่านระบบ
  * ยืนยัน OTP เพื่อตั้งรหัสผ่านใหม่ได้อย่างปลอดภัย

---

### 2. 👗 ระบบสำหรับผู้ใช้งาน/ลูกค้า (User Features)
* **หน้ารายการชุดเช่า (Costume Catalog):** แสดงรายการชุดเช่าทั้งหมด พร้อมหมวดหมู่ ราคา และสถานะความพร้อม
* **ดูรายละเอียดชุด (Costume Detail):** แสดงรูปภาพ รายละเอียดชุด ราคาเช่า ค่ามัดจำ และสถานะชุด
* **ทำรายการเช่าชุด (Rental Process):** กรอกข้อมูลวันเริ่มเช่า-วันคืน พร้อมคำนวณราคารวมอัตโนมัติ
* **ติดตามสถานะการเช่า (Order Tracking):** ตรวจสอบประวัติการเช่า สถานะออเดอร์ และเลขพัสดุ (Tracking Number)
* **จัดการข้อมูลส่วนตัว (User Profile):** ดูและอัปเดตข้อมูลส่วนตัว (ชื่อ, อีเมล, เบอร์โทรศัพท์, ที่อยู่)

---

### 3. 🛠 ระบบสำหรับผู้ดูแลระบบ (Admin Features)
* **แดชบอร์ดสรุปภาพรวม (Admin Dashboard):** แสดงรายการเช่าและสถานะชุดทั้งหมดในระบบ
* **จัดการข้อมูลชุดเช่า (Costume Management):**
  * เพิ่มชุดเช่าใหม่ พร้อมอัปโหลดรูปภาพสินค้า
  * แก้ไขข้อมูลชุด ราคา ค่ามัดจำ และสถานะ
  * ลบข้อมูลชุดเช่าออกจากระบบ
* **จัดการการจัดส่งพัสดุ (Parcel & Tracking Management):** อัปเดตชื่อบริษัทขนส่งและเลข Tracking Number ให้กับออเดอร์ลูกค้า
* **ระบบรับคืนชุด (Costume Return):** บันทึกการรับคืนชุดเช่าเมื่อลูกค้าส่งชุดกลับ และคืนสถานะชุดให้พร้อมเช่าใหม่

---

## 🛠️ เทคโนโลยีที่ใช้ (Tech Stack)

* **Backend:** Java (JDK 17/21), Spring Boot (Spring Web, Spring Data JPA, Spring Security)
* **Frontend:** Thymeleaf, HTML5, CSS3, JavaScript, Bootstrap
* **Database:** PostgreSQL / MySQL
* **Build Tool:** Maven

---

## 📂 โครงสร้างโปรเจกต์ (Project Structure)

```text
src/main/java/com/example/costumerentalsystem/
├── config/               # ไฟล์ตั้งค่าระบบ (Security, Web Mappings)
├── controller/           # Controller จัดการ Request (Admin, User, Costume, Rental, Auth)
├── model/                # Entity Classes (User, Costume, Rental, Payment ฯลฯ)
├── repository/           # Data Access Layer (Spring Data JPA)
└── service/              # Business Logic Layer
```
---
## 🚀 วิธีการติดตั้งและใช้งาน (Getting Started)

### 1. Prerequisites
* Java Development Kit (JDK) 17 ขึ้นไป
* Maven
* PostgreSQL / MySQL Database

---

### 2. ตั้งค่า Database
แก้ไขไฟล์ `src/main/resources/application.properties` ให้ตรงกับฐานข้อมูลของคุณ:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/costume_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
3. รันโปรเจกต์
เปิด Terminal ในโฟลเดอร์โปรเจกต์แล้วรันคำสั่ง:

```Bash
./mvnw spring-boot:run
```
เปิด Browser แล้วเข้าไปที่: http://localhost:8081
