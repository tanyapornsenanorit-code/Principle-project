# 5. Activity Diagram

## 5.1 Activity Diagram: Costume Rental & Return Process

```mermaid
flowchart TD
    Start([เริ่มต้น]) --> Search[ค้นหาและเลือกชุดที่ต้องการเช่า]
    Search --> CheckAvailability{ตรวจสอบความพร้อมของชุด?}
    
    CheckAvailability -- ไม่พร้อมใช้งาน --> SelectOther[เลือกชุดอื่น หรือเปลี่ยนวันที่เช่า]
    SelectOther --> Search
    
    CheckAvailability -- พร้อมใช้งาน --> FillDetails[กรอกช่วงวันเช่า-คืน และข้อมูลผู้เช่า]
    FillDetails --> CreateOrder[ระบบสร้างคำสั่งเช่า สถานะ: PENDING]
    CreateOrder --> Pay[ชำระเงินตามยอดรวม]
    
    Pay --> CheckPayment{ชำระเงินสำเร็จหรือไม่?}
    
    CheckPayment -- ล้มเหลว / ยกเลิก --> CancelOrder[ระบบยกเลิกคำสั่งเช่า]
    CancelOrder --> EndOrder([จบการทำงาน])
    
    CheckPayment -- สำเร็จ --> ActiveOrder[ระบบอัปเดตสถานะคำสั่งเช่าเป็น: ACTIVE]
    ActiveOrder --> Pickup[ลูกค้ามารับชุด / จัดส่งชุด]
    Pickup --> Return[ลูกค้าคืนชุดตามกำหนดเวลา]
    
    Return --> Inspect{ตรวจสอบสภาพชุด?}
    
    Inspect -- ชำรุด/สูญหาย --> ChargeFine[คำนวณและเรียกเก็บค่าปรับ]
    ChargeFine --> CompleteOrder
    
    Inspect -- สภาพปกติ --> CompleteOrder[ระบบอัปเดตสถานะเป็น: COMPLETED]
    CompleteOrder --> UpdateInventory[อัปเดตสถานะชุดเป็นพร้อมใช้งาน]
    UpdateInventory --> EndOrder

```
## 5.2 Description
Activity Diagram แสดงขั้นตอนการทำงาน (Workflow) ทั้งหมดของกระบวนการเช่าชุด:

การเลือกชุด: ลูกค้าทำการค้นหาและตรวจสอบความพร้อมของชุด

การทำรายการเช่า: หากชุดพร้อมใช้งาน ลูกค้าจะทำการกรอกข้อมูล และระบบจะสร้าง Order

การประมวลผลการชำระเงิน: หากชำระเงินสำเร็จ สถานะเปลี่ยนเป็น ACTIVE

การคืนชุดและตรวจรับ: เมื่อลูกค้านำชุดมาคืน เจ้าหน้าที่ตรวจสอบสภาพชุด หากปกติหรือชำระค่าปรับเรียบร้อยแล้ว ระบบจะอัปเดตสถานะเป็น COMPLETED และเปลี่ยนสถานะชุดกลับมาพร้อมเช่าอีกครั้ง