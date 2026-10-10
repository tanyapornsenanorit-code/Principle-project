package com.example.costumerentalsystem.controller;

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
@RequestMapping("/reset-password")
public class PasswordResetController {

    private final UserAccountService userAccountService;

    public PasswordResetController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public String showResetPasswordForm(
            HttpSession session,
            Model model) {

        String email = (String) session.getAttribute("resetEmail");

        if (email == null) {
            return "redirect:/forgot-password";
        }

        model.addAttribute("email", email);
        return "reset-password";
    }

    @PostMapping
    public String processResetPassword(
            @RequestParam("otp") String otp,
            @RequestParam("newPassword") String newPassword,
            HttpSession session,
            Model model) {

        String sessionOtp = (String) session.getAttribute("resetOtp");
        String sessionEmail = (String) session.getAttribute("resetEmail");

        if (sessionOtp == null
                || sessionEmail == null
                || !sessionOtp.equals(otp)) {
            model.addAttribute("error", "รหัส OTP ไม่ถูกต้องหรือหมดอายุ");
            if (sessionEmail != null) {
                model.addAttribute("email", sessionEmail);
            }
            return "reset-password";
        }

        try {
            userAccountService.changePassword(sessionEmail, newPassword);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("error", "ไม่พบผู้ใช้งานนี้ในระบบ");
            model.addAttribute("email", sessionEmail);
            return "reset-password";
        }

        session.removeAttribute("resetOtp");
        session.removeAttribute("resetEmail");

        return "redirect:/login?resetSuccess=true";
    }
}
