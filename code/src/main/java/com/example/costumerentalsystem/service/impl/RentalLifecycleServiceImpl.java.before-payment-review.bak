package com.example.costumerentalsystem.service.impl;

import java.time.LocalDateTime;
import java.util.function.Function;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.costumerentalsystem.domain.entity.Payment;
import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.entity.Shipment;
import com.example.costumerentalsystem.domain.enums.PaymentStatus;
import com.example.costumerentalsystem.domain.enums.RentalStatus;
import com.example.costumerentalsystem.dto.request.PaymentRequest;
import com.example.costumerentalsystem.dto.request.ShipmentRequest;
import com.example.costumerentalsystem.dto.response.RentalResponse;
import com.example.costumerentalsystem.exception.ResourceNotFoundException;
import com.example.costumerentalsystem.mapper.RentalMapper;
import com.example.costumerentalsystem.repository.RentalRepository;
import com.example.costumerentalsystem.service.RentalLifecycleService;
import com.example.costumerentalsystem.service.event.RentalStatusChangedEvent;
import com.example.costumerentalsystem.service.state.RentalState;
import com.example.costumerentalsystem.service.state.RentalStateFactory;

@Service
@Transactional
public class RentalLifecycleServiceImpl implements RentalLifecycleService {

    private final RentalRepository rentalRepository;
    private final RentalStateFactory stateFactory;
    private final RentalMapper rentalMapper;
    private final ApplicationEventPublisher eventPublisher;

    public RentalLifecycleServiceImpl(RentalRepository rentalRepository,
                                      RentalStateFactory stateFactory,
                                      RentalMapper rentalMapper,
                                      ApplicationEventPublisher eventPublisher) {
        this.rentalRepository = rentalRepository;
        this.stateFactory = stateFactory;
        this.rentalMapper = rentalMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public RentalResponse pay(Long rentalId, PaymentRequest request) {
        Rental rental = find(rentalId);
        RentalStatus from = rental.getStatus();
        RentalStatus to = stateFactory.stateOf(from).pay();

        Payment payment = new Payment();
        payment.setAmount(rental.getTotalPrice().add(rental.getDepositAmount()));
        payment.setPaymentMethod(request.method().name());
        payment.setPaymentType("RENTAL_FEE_AND_DEPOSIT");
        payment.setSlipImageUrl(request.slipImageUrl());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.VERIFIED);
        rental.setPayment(payment);

        return apply(rental, from, to);
    }

    @Override
    public RentalResponse ship(Long rentalId, ShipmentRequest request) {
        Rental rental = find(rentalId);
        RentalStatus from = rental.getStatus();
        RentalStatus to = stateFactory.stateOf(from).ship();

        Shipment shipment = new Shipment();
        shipment.setCourier(request.courier().trim());
        shipment.setTrackingNo(request.trackingNo().trim());
        shipment.setShippedAt(LocalDateTime.now());
        rental.setShipment(shipment);

        return apply(rental, from, to);
    }

    @Override
    public RentalResponse startUse(Long rentalId) {
        return simpleTransition(rentalId, RentalState::startUse);
    }

    @Override
    public RentalResponse returnItem(Long rentalId) {
        return simpleTransition(rentalId, RentalState::returnItem);
    }

    @Override
    public RentalResponse complete(Long rentalId) {
        return simpleTransition(rentalId, RentalState::complete);
    }

    @Override
    public RentalResponse cancel(Long rentalId) {
        return simpleTransition(rentalId, RentalState::cancel);
    }

    private RentalResponse simpleTransition(Long rentalId, Function<RentalState, RentalStatus> action) {
        Rental rental = find(rentalId);
        RentalStatus from = rental.getStatus();
        RentalStatus to = action.apply(stateFactory.stateOf(from));
        return apply(rental, from, to);
    }

    private RentalResponse apply(Rental rental, RentalStatus from, RentalStatus to) {
        rental.setStatus(to);
        rentalRepository.save(rental);
        eventPublisher.publishEvent(new RentalStatusChangedEvent(
                rental.getId(), rental.getCostume().getId(), rental.getUser().getEmail(), from, to));
        return rentalMapper.toResponse(rental);
    }

    private Rental find(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("รายการเช่า", rentalId));
    }
}