package com.example.costumerentalsystem.service;

public interface NotificationService {

    // ถ้า to ว่างก็ข้าม ส่งไม่สำเร็จก็แค่ log จะได้ไม่กระทบงานหลัก
    void send(String to, String subject, String body);

    void sendOtp(String to, String otp);
}
