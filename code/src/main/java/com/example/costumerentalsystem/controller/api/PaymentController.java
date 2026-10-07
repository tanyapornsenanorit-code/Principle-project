package com.example.costumerentalsystem.controller.api;

import com.example.costumerentalsystem.dto.request.PaymentRequest;
import com.example.costumerentalsystem.dto.response.RentalResponse;
import com.example.costumerentalsystem.service.RentalLifecycleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final RentalLifecycleService rentalLifecycleService;

    // ใช้ Constructor มาตรฐานเพื่อไม่ให้ติดปัญหา Lombok ใน VS Code
    public PaymentController(RentalLifecycleService rentalLifecycleService) {
        this.rentalLifecycleService = rentalLifecycleService;
    }

    @PostMapping("/rentals/{rentalId}")
    public ResponseEntity<RentalResponse> processPayment(
            @PathVariable Long rentalId,
            @Valid @RequestBody PaymentRequest request) {
        // เปลี่ยนมาเรียกใช้ .pay() ให้ตรงกับ RentalLifecycleService
        RentalResponse response = rentalLifecycleService.pay(rentalId, request);
        return ResponseEntity.ok(response);
    }
}