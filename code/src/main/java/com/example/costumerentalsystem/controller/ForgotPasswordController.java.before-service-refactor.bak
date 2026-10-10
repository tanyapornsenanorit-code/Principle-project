package com.example.costumerentalsystem.controller;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.repository.UserRepository;
import com.example.costumerentalsystem.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // GET /forgot-password : แสดงหน้ากรอกอีเมลเพื่อขอรับ OTP
    @GetMapping
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    // 🟢 POST /forgot-password/send-otp : ปิดการส่งอีเมลจริง แล้วแสดง OTP ใน Terminal แทน
    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email, HttpSession session, Model model) {
        // ค้นหาผู้ใช้จาก email หรือ username
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = userRepository.findByUsername(email);
        }

        if (user == null) {
            model.addAttribute("error", "ไม่พบอีเมลนี้ในระบบ");
            return "forgot-password";
        }

        // สุ่ม OTP 6 หลัก
        String otp = String.format("%06d", new Random().nextInt(999999));
        session.setAttribute("resetOtp", otp);
        session.setAttribute("resetEmail", email);

        // 🟢 [จุดที่แก้ไข] ปริ้นท์ OTP ออกทาง Terminal ฝั่ง VS Code แทนการส่งอีเมลจริง
        System.out.println("==================================================");
        System.out.println("🔑 [MOCK OTP] รหัสสำหรับ " + email + " คือ: " + otp);
        System.out.println("==================================================");

        // 🟢 เปลี่ยนสถานะหน้าเว็บไปฟอร์มกรอก OTP ทันที
        model.addAttribute("otpSent", true);
        model.addAttribute("email", email);
        model.addAttribute("message", "ส่งรหัส OTP เรียบร้อยแล้ว (ดูรหัส 6 หลักใน Terminal)");
        return "forgot-password";
    }

    // POST /forgot-password/reset : ตรวจสอบ OTP และบันทึกรหัสผ่านใหม่
    @PostMapping("/reset")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("otp") String otp,
                                @RequestParam("newPassword") String newPassword,
                                HttpSession session,
                                Model model) {
        
        String sessionOtp = (String) session.getAttribute("resetOtp");
        String sessionEmail = (String) session.getAttribute("resetEmail");

        // ตรวจสอบความถูกต้องของ OTP และ Email
        if (sessionOtp == null || sessionEmail == null || !sessionOtp.equals(otp) || !sessionEmail.equals(email)) {
            model.addAttribute("error", "รหัส OTP ไม่ถูกต้องหรือหมดอายุ");
            model.addAttribute("otpSent", true);
            model.addAttribute("email", email);
            return "forgot-password";
        }

        // ค้นหาผู้ใช้เพื่ออัปเดตรหัสผ่าน
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = userRepository.findByUsername(email);
        }

        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // เคลียร์ค่าใน Session
            session.removeAttribute("resetOtp");
            session.removeAttribute("resetEmail");

            model.addAttribute("message", "เปลี่ยนรหัสผ่านสำเร็จ กรุณาเข้าสู่ระบบด้วยรหัสผ่านใหม่");
            return "login";
        } else {
            model.addAttribute("error", "ไม่พบผู้ใช้งานนี้ในระบบ");
            return "forgot-password";
        }
    }
}