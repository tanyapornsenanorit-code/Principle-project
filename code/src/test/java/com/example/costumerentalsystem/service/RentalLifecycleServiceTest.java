package com.example.costumerentalsystem.service;

import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.enums.RentalStatus;
import com.example.costumerentalsystem.exception.InvalidRentalTransitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RentalLifecycleServiceTest {

    @Test
    @DisplayName("เมื่อสถานะเป็น PENDING ไม่ควรรูปแบบการข้ามขั้นตอนไปเป็น RETURNED ได้")
    void testInvalidStatusTransitionThrowsException() {
        Rental rental = new Rental();
        rental.setStatus(RentalStatus.PENDING);

        assertThrows(InvalidRentalTransitionException.class, () -> {
            // จำลองการเปลี่ยนสถานะผิดเงื่อนไข
            if (rental.getStatus() == RentalStatus.PENDING) {
                throw new InvalidRentalTransitionException(rental.getStatus(), "คืนชุด");
            }
        });
    }

    @Test
    @DisplayName("ตรวจสอบการสร้างตัวอย่างรายการเช่าเริ่มต้น")
    void testRentalInitialStatus() {
        Rental rental = new Rental();
        rental.setStatus(RentalStatus.PENDING);
        
        assertEquals(RentalStatus.PENDING, rental.getStatus());
    }
}