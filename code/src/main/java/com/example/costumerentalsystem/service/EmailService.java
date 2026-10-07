package com.example.costumerentalsystem.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}