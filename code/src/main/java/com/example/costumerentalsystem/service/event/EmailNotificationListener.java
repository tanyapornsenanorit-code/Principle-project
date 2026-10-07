package com.example.costumerentalsystem.service.event;

import com.example.costumerentalsystem.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationListener {

    private final EmailService emailService;

    public EmailNotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleRentalStatusChanged(RentalStatusChangedEvent event) {
        String subject = "อัปเดตสถานะการเช่าชุด - รายการ #" + event.rentalId();
        String content = String.format("สวัสดีครับ/ค่ะ,\n\nรายการเช่าชุดของคุณได้รับการอัปเดตสถานะจาก %s เป็น %s เรียบร้อยแล้ว",
                event.fromStatus(), event.toStatus());

        emailService.sendEmail(event.userEmail(), subject, content);
    }
}