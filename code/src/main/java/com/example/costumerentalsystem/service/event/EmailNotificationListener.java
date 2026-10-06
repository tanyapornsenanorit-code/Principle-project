package com.example.costumerentalsystem.service.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.costumerentalsystem.service.NotificationService;

// ส่งอีเมลหลัง commit เสร็จเท่านั้น ถ้า rollback จะได้ไม่ส่งเมลผิด
@Component
public class EmailNotificationListener {

    private final NotificationService notificationService;

    public EmailNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRentalStatusChanged(RentalStatusChangedEvent event) {
        String subject = "อัปเดตการเช่า #" + event.rentalId();
        String body = "สถานะการเช่า #" + event.rentalId() + " ของคุณเปลี่ยนเป็น: " + event.to().getDisplayName();
        notificationService.send(event.userEmail(), subject, body);
    }
}
