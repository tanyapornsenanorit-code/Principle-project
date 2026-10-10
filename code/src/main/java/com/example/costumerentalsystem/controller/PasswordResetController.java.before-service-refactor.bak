package com.example.costumerentalsystem.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reset-password")
public class PasswordResetController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // GET /reset-password : แสดงหน้ากรอก OTP และรหัสผ่านใหม่
    @GetMapping
    public String showResetPasswordForm(HttpSession session, Model model) {
        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            return "redirect:/forgot-password";
        }
        model.addAttribute("email", email);
        return "reset-password";
    }

    // POST /reset-password : ยืนยัน OTP และบันทิกรหัสผ่านใหม่ลงฐานข้อมูล
    @PostMapping
    public String processResetPassword(@RequestParam("otp") String otp,
                                       @RequestParam("newPassword") String newPassword,
                                       HttpSession session,
                                       Model model) {
        String sessionOtp = (String) session.getAttribute("resetOtp");
        String sessionEmail = (String) session.getAttribute("resetEmail");

        if (sessionOtp == null || !sessionOtp.equals(otp)) {
            model.addAttribute("error", "รหัส OTP ไม่ถูกต้อง");
            return "reset-password";
        }

        User user = userRepository.findByUsername(sessionEmail);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // เคลียร์ข้อมูลใน Session
            session.removeAttribute("resetOtp");
            session.removeAttribute("resetEmail");

            return "redirect:/login?resetSuccess=true";
        }

        model.addAttribute("error", "เกิดข้อผิดพลาด กรุณาลองใหม่อีกครั้ง");
        return "reset-password";
    }
}