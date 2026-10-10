package com.example.costumerentalsystem.service;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.costumerentalsystem.domain.enums.RentalStatus;
import com.example.costumerentalsystem.dto.request.RentalCreateRequest;
import com.example.costumerentalsystem.dto.response.RentalQuoteResponse;
import com.example.costumerentalsystem.dto.response.RentalResponse;

public interface RentalService {

    RentalQuoteResponse quote(
            Long costumeId,
            LocalDate startDate,
            LocalDate endDate
    );

    RentalResponse create(
            Long userId,
            RentalCreateRequest request
    );

    RentalResponse getById(
            Long rentalId
    );

    Page<RentalResponse> findByUser(
            Long userId,
            Pageable pageable
    );

    Page<RentalResponse> findAll(
            RentalStatus status,
            Pageable pageable
    );
}
