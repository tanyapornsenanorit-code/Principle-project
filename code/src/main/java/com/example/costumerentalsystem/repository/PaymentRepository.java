package com.example.costumerentalsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.costumerentalsystem.domain.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /** Rental กับ Payment เป็น 1-1 จึงคืน Optional (เดิมคืน List) */
    Optional<Payment> findByRentalId(Long rentalId);
}
