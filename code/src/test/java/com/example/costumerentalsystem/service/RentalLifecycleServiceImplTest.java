package com.example.costumerentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.entity.Payment;
import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.domain.enums.PaymentMethod;
import com.example.costumerentalsystem.domain.enums.PaymentStatus;
import com.example.costumerentalsystem.domain.enums.RentalStatus;
import com.example.costumerentalsystem.dto.request.PaymentRequest;
import com.example.costumerentalsystem.exception.ConflictException;
import com.example.costumerentalsystem.mapper.RentalMapper;
import com.example.costumerentalsystem.repository.RentalRepository;
import com.example.costumerentalsystem.service.impl.RentalLifecycleServiceImpl;
import com.example.costumerentalsystem.service.event.RentalStatusChangedEvent;
import com.example.costumerentalsystem.service.state.RentalState;
import com.example.costumerentalsystem.service.state.RentalStateFactory;

@ExtendWith(MockitoExtension.class)
class RentalLifecycleServiceImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private RentalStateFactory stateFactory;

    @Mock
    private RentalMapper rentalMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private RentalState pendingPaymentState;

    @InjectMocks
    private RentalLifecycleServiceImpl lifecycleService;

    @Test
    void submittingPaymentShouldRemainPendingAndUseServerCalculatedAmount() {
        Rental rental = newRental();
        when(rentalRepository.findById(1L))
                .thenReturn(Optional.of(rental));

        PaymentRequest request =
                new PaymentRequest(PaymentMethod.PROMPTPAY, "/uploads/slip.png");

        lifecycleService.submitPayment(1L, request);

        assertEquals(RentalStatus.PENDING_PAYMENT, rental.getStatus());
        assertEquals(PaymentStatus.PENDING, rental.getPayment().getStatus());
        assertEquals(new BigDecimal("150.00"), rental.getPayment().getAmount());
        assertEquals("PROMPTPAY", rental.getPayment().getPaymentMethod());
        assertEquals("/uploads/slip.png", rental.getPayment().getSlipImageUrl());

        verify(rentalRepository).save(rental);
    }

    @Test
    void adminApprovalShouldVerifyPaymentAndTransitionToPaid() {
        Rental rental = newRental();
        Payment payment = newPendingPayment();
        rental.setPayment(payment);

        when(rentalRepository.findById(1L))
                .thenReturn(Optional.of(rental));
        when(stateFactory.stateOf(RentalStatus.PENDING_PAYMENT))
                .thenReturn(pendingPaymentState);
        when(pendingPaymentState.pay())
                .thenReturn(RentalStatus.PAID);

        lifecycleService.verifyPayment(1L);

        assertEquals(PaymentStatus.VERIFIED, payment.getStatus());
        assertEquals(RentalStatus.PAID, rental.getStatus());

        verify(rentalRepository).save(rental);
        verify(eventPublisher).publishEvent(any(RentalStatusChangedEvent.class));
    }

    @Test
    void adminRejectionShouldKeepRentalPendingAndMarkPaymentRejected() {
        Rental rental = newRental();
        Payment payment = newPendingPayment();
        rental.setPayment(payment);

        when(rentalRepository.findById(1L))
                .thenReturn(Optional.of(rental));

        lifecycleService.rejectPayment(1L);

        assertEquals(PaymentStatus.REJECTED, payment.getStatus());
        assertEquals(RentalStatus.PENDING_PAYMENT, rental.getStatus());

        verify(rentalRepository).save(rental);
        verify(eventPublisher, never())
                .publishEvent(any(RentalStatusChangedEvent.class));
    }

    @Test
    void approvalShouldFailWhenThereIsNoPaymentAwaitingReview() {
        Rental rental = newRental();
        when(rentalRepository.findById(1L))
                .thenReturn(Optional.of(rental));

        assertThrows(
                ConflictException.class,
                () -> lifecycleService.verifyPayment(1L));

        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void duplicatePaymentSubmissionShouldBeRejectedWhilePendingReview() {
        Rental rental = newRental();
        rental.setPayment(newPendingPayment());

        when(rentalRepository.findById(1L))
                .thenReturn(Optional.of(rental));

        PaymentRequest request =
                new PaymentRequest(PaymentMethod.PROMPTPAY, "/uploads/new-slip.png");

        assertThrows(
                ConflictException.class,
                () -> lifecycleService.submitPayment(1L, request));

        verify(rentalRepository, never()).save(any(Rental.class));
    }

    private Rental newRental() {
        User user = new User();
        user.setId(7L);
        user.setUsername("test-user");
        user.setEmail("test@example.com");

        Costume costume = new Costume();
        costume.setId(11L);
        costume.setName("Test Costume");

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setUser(user);
        rental.setCostume(costume);
        rental.setStatus(RentalStatus.PENDING_PAYMENT);
        rental.setTotalPrice(new BigDecimal("120.00"));
        rental.setDepositAmount(new BigDecimal("30.00"));

        return rental;
    }

    private Payment newPendingPayment() {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("150.00"));
        payment.setPaymentMethod("PROMPTPAY");
        payment.setStatus(PaymentStatus.PENDING);
        payment.setSlipImageUrl("/uploads/slip.png");
        return payment;
    }
}
