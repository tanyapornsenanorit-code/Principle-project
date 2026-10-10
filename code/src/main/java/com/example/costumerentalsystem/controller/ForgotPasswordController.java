package com.example.costumerentalsystem.controller;

import java.security.SecureRandom;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.costumerentalsystem.exception.ResourceNotFoundException;
import com.example.costumerentalsystem.service.UserAccountService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserAccountService userAccountService;

    public ForgotPasswordController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/send-otp")
    public String sendOtp(
            @RequestParam("email") String email,
            HttpSession session,
            Model model) {

        String identifier = email == null ? "" : email.trim();

        if (identifier.isBlank()) {
            model.addAttribute("error", "กรุณาระบุอีเมลหรือชื่อผู้ใช้");
            return "forgot-password";
        }

        try {
            userAccountService.findByEmailOrUsername(identifier);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("error", "ไม่พบอีเมลนี้ในระบบ");
            return "forgot-password";
        }

        String otp = String.format(
                "%06d", SECURE_RANDOM.nextInt(1_000_000));

        session.setAttribute("resetOtp", otp);
        session.setAttribute("resetEmail", identifier);

        // โหมดทดสอบ: แสดง OTP ใน Terminal ไม่ได้ส่งอีเมลจริง
        System.out.println("==================================================");
        System.out.println("[MOCK OTP] รหัสสำหรับ " + identifier + " คือ: " + otp);
        System.out.println("==================================================");

        model.addAttribute("otpSent", true);
        model.addAttribute("email", identifier);
        model.addAttribute(
                "message",
                "สร้างรหัส OTP แล้ว (ดูรหัส 6 หลักใน Terminal)");

        return "forgot-password";
    }

    @PostMapping("/reset")
    public String resetPassword(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp,
            @RequestParam("newPassword") String newPassword,
            HttpSession session,
            Model model) {

        String sessionOtp = (String) session.getAttribute("resetOtp");
        String sessionEmail = (String) session.getAttribute("resetEmail");
        String identifier = email == null ? "" : email.trim();

        if (sessionOtp == null
                || sessionEmail == null
                || !sessionOtp.equals(otp)
                || !sessionEmail.equals(identifier)) {
            model.addAttribute("error", "รหัส OTP ไม่ถูกต้องหรือหมดอายุ");
            model.addAttribute("otpSent", true);
            model.addAttribute("email", identifier);
            return "forgot-password";
        }

        try {
            userAccountService.changePassword(identifier, newPassword);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("error", "ไม่พบผู้ใช้งานนี้ในระบบ");
            model.addAttribute("otpSent", true);
            model.addAttribute("email", identifier);
            return "forgot-password";
        }

        session.removeAttribute("resetOtp");
        session.removeAttribute("resetEmail");

        model.addAttribute(
                "message",
                "เปลี่ยนรหัสผ่านสำเร็จ กรุณาเข้าสู่ระบบด้วยรหัสผ่านใหม่");

        return "login";
    }
}
