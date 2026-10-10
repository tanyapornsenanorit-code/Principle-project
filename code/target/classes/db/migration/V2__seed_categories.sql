-- V2: หมวดหมู่เริ่มต้น (ตรงกับตัวเลือกใน dropdown ของหน้า admin เดิม)
-- ใช้ชื่อไฟล์ V2__seed_categories เพื่อไม่ชนกับ V3__seed_data ที่จะเพิ่มในรอบ A2
INSERT INTO categories (name) VALUES
    ('ชุดไทย'),
    ('ชุดสูท'),
    ('ชุดราตรี'),
    ('ชุดแฟนซี'),
    ('อื่น ๆ')
ON CONFLICT (name) DO NOTHING;
