package com.example.costumerentalsystem.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("รหัส OTP สำหรับตั้งรหัสผ่านใหม่ - Costume Rental System");
        message.setText("รหัส OTP สำหรับยืนยันการเปลี่ยนรหัสผ่านของคุณคือ: " + otp + "\n\nกรุณานำรหัสนี้ไปกรอกในหน้าเปลี่ยนรหัสผ่าน");
        mailSender.send(message);
    }
}