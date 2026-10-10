package com.example.costumerentalsystem.controller;

import com.example.costumerentalsystem.domain.entity.Payment;
import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.enums.PaymentStatus;
import com.example.costumerentalsystem.domain.enums.RentalStatus;
import com.example.costumerentalsystem.repository.PaymentRepository;
import com.example.costumerentalsystem.repository.RentalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;

    public PaymentController(RentalRepository rentalRepository, PaymentRepository paymentRepository) {
        this.rentalRepository = rentalRepository;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/pay/{rentalId}")
    public String showPaymentPage(@PathVariable Long rentalId, Model model) {
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        model.addAttribute("rental", rental);
        return "user/payment";
    }

    @PostMapping("/submit")
    public String submitPayment(@RequestParam Long rentalId,
                                @RequestParam String paymentMethod,
                                @RequestParam BigDecimal amount,
                                @RequestParam(value = "slipFile", required = false) MultipartFile slipFile) {
        rentalRepository.findById(rentalId).ifPresent(rental -> {
            Payment payment = paymentRepository.findByRentalId(rentalId).orElseGet(Payment::new);
            payment.setRental(rental);
            payment.setAmount(amount);
            payment.setPaymentMethod(paymentMethod);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setStatus(PaymentStatus.VERIFIED);

            if (slipFile != null && !slipFile.isEmpty()) {
                payment.setSlipImageUrl(slipFile.getOriginalFilename());
            }

            paymentRepository.save(payment);
            rental.setStatus(RentalStatus.PAID);
            rentalRepository.save(rental);
        });

        return "user/payment-success";
    }
}