-- V3: ชุดตัวอย่างสำหรับทดสอบ (ต่อจาก V2__seed_categories)
-- ไม่สร้างแอดมินที่นี่ ให้ AdminSeeder สร้างตอนเริ่มระบบ โดยอ่านรหัสผ่านจาก ADMIN_PASSWORD

INSERT INTO costumes (name, category_id, price_per_day, description, status) VALUES
    ('ชุดไทยจิตรลดา',          (SELECT id FROM categories WHERE name = 'ชุดไทย'),   800.00, 'ชุดไทยสไบปักลายทอง',            'AVAILABLE'),
    ('สูทสีดำคลาสสิก',          (SELECT id FROM categories WHERE name = 'ชุดสูท'),   500.00, 'สูทสีดำทรงสลิม เหมาะกับงานทางการ', 'AVAILABLE'),
    ('ชุดราตรีสีแดงเลือดนก',     (SELECT id FROM categories WHERE name = 'ชุดราตรี'), 1200.00, 'ชุดราตรียาวเปิดไหล่',            'AVAILABLE'),
    ('ชุดแฟนซีเจ้าหญิง',        (SELECT id FROM categories WHERE name = 'ชุดแฟนซี'),  600.00, 'ชุดเจ้าหญิงสำหรับงานปาร์ตี้',       'AVAILABLE');
