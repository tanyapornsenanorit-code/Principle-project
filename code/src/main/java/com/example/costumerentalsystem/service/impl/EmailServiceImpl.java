package com.example.costumerentalsystem.service.impl;

import com.example.costumerentalsystem.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String body) {
        // จำลองการส่งอีเมลสำหรับระบบ
        System.out.printf("[Email Sent] To: %s | Subject: %s%n", to, subject);
    }
}