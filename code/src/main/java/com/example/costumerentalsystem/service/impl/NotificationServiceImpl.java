package com.example.costumerentalsystem.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.costumerentalsystem.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final JavaMailSender mailSender;

    public NotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String to, String subject, String body) {
        if (to == null || to.isBlank()) {
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (MailException ex) {
            // ส่งเมลไม่ได้ก็ไม่ควรทำให้งานหลักพัง
            log.warn("ส่งอีเมลถึง {} ไม่สำเร็จ: {}", to, ex.getMessage());
        }
    }

    @Override
    public void sendOtp(String to, String otp) {
        send(to,
                "รหัส OTP สำหรับตั้งรหัสผ่านใหม่ - Costume Rental System",
                "รหัส OTP สำหรับยืนยันการเปลี่ยนรหัสผ่านของคุณคือ: " + otp
                        + "\n\nกรุณานำรหัสนี้ไปกรอกในหน้าเปลี่ยนรหัสผ่าน");
    }
}
