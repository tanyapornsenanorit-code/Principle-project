package com.example.costumerentalsystem.controller.api;

import com.example.costumerentalsystem.dto.request.ShipmentRequest;
import com.example.costumerentalsystem.dto.response.RentalResponse;
import com.example.costumerentalsystem.service.RentalLifecycleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shipments")
public class ShipmentController {

    private final RentalLifecycleService rentalLifecycleService;

    public ShipmentController(RentalLifecycleService rentalLifecycleService) {
        this.rentalLifecycleService = rentalLifecycleService;
    }

    @PostMapping("/rentals/{rentalId}")
    public ResponseEntity<RentalResponse> shipRental(
            @PathVariable Long rentalId,
            @Valid @RequestBody ShipmentRequest request) {
        RentalResponse response = rentalLifecycleService.ship(rentalId, request);
        return ResponseEntity.ok(response);
    }
}