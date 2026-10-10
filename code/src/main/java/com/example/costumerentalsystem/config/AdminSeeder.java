package com.example.costumerentalsystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.domain.entity.UserProfile;
import com.example.costumerentalsystem.domain.enums.Role;
import com.example.costumerentalsystem.repository.UserRepository;

/**
 * คลาส AdminSeeder ทำหน้าที่สร้างบัญชีผู้ดูแลระบบ (Admin) บัญชีแรกโดยอัตโนมัติ
 * เมื่อแอปพลิเคชันเริ่มทำงาน (Application Startup) หากในระบบยังไม่มีบัญชีแอดมินอยู่เลย
 * 
 * ข้อมูลการเข้าใช้งาน (เช่น รหัสผ่าน) จะถูกดึงมาจาก Environment Variables หรือ Config 
 * เพื่อป้องกันการ Hardcode รหัสผ่านลงใน Source Code และหลีกเลี่ยงการนำข้อมูลสุ่มเสี่ยงขึ้น Git
 */
@Component
public class AdminSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    // Repository สำหรับจัดการข้อมูลผู้ใช้ในฐานข้อมูล
    private final UserRepository userRepository;
    
    // เครื่องมือสำหรับเข้ารหัสพาสเวิร์ด (เช่น BCrypt) ก่อนเซฟลงฐานข้อมูล
    private final PasswordEncoder passwordEncoder;
    
    // ค่าคอนฟิกสำหรับบัญชีแอดมิน (ดึงมาจาก application.properties หรือ Environment Variable)
    private final String username;
    private final String password;
    private final String email;

    /**
     * Constructor Injection สำหรับฉีด Dependency และดึงค่า Config ต่างๆ
     * 
     * @param userRepository Repository จัดการข้อมูล User
     * @param passwordEncoder ตัวเข้ารหัสรหัสผ่าน
     * @param username ชื่อผู้ใช้แอดมิน (ค่าเริ่มต้นคือ 'admin' หากไม่ได้กำหนดไว้)
     * @param password รหัสผ่านแอดมิน (ดึงจาก app.admin.password / ADMIN_PASSWORD)
     * @param email อีเมลของแอดมิน (ดึงจาก app.admin.email)
     */
    public AdminSeeder(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${app.admin.username:admin}") String username,
                       @Value("${app.admin.password:}") String password,
                       @Value("${app.admin.email:}") String email) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * เมธอด run() จะทำงานอัตโนมัติทันทีที่ Spring Boot เริ่มต้นระบบเสร็จสิ้น (ผ่าน ApplicationRunner)
     */
    @Override
    public void run(ApplicationArguments args) {
        // 1. ตรวจสอบว่ามีผู้ใช้งานที่มีสิทธิ์ ADMIN ในฐานข้อมูลอยู่แล้วหรือไม่
        if (userRepository.existsByRole(Role.ADMIN)) {
            // หากมีแอดมินในระบบแล้ว ไม่ต้องทำการสร้างใหม่ ยุติการทำงานทันที
            return;
        }

        // 2. ตรวจสอบว่ามีการตั้งค่ารหัสผ่านสำหรับแอดมินหรือไม่
        if (password == null || password.isBlank()) {
            log.warn("ยังไม่มีแอดมินและไม่ได้ตั้ง ADMIN_PASSWORD เลยไม่ได้สร้างให้");
            return;
        }

        // 3. ตรวจสอบว่า username ที่จะใช้สร้างแอดมิน มีผู้ใช้อื่น (ที่ไม่ใช่แอดมิน) ใช้งานไปแล้วหรือยัง
        if (userRepository.existsByUsername(username)) {
            log.warn("username '{}' มีคนใช้อยู่แล้ว (ไม่ใช่แอดมิน) เลยไม่ได้สร้างแอดมินให้", username);
            return;
        }

        // 4. สร้าง Entity ของ User สำหรับแอดมิน พร้อมเข้ารหัสรหัสผ่านผ่าน passwordEncoder
        User admin = new User(username, passwordEncoder.encode(password), Role.ADMIN);
        admin.setEmail(email);

        // 5. สร้างข้อมูลโปรไฟล์พื้นฐานสำหรับแอดมิน
        UserProfile profile = new UserProfile();
        profile.setFullName("ผู้ดูแลระบบ");
        admin.setProfile(profile);

        // 6. บันทึกข้อมูลแอดมินคนสแกนลงฐานข้อมูล
        userRepository.save(admin);
        log.info("สร้างแอดมิน '{}' เรียบร้อย", username);
    }
}