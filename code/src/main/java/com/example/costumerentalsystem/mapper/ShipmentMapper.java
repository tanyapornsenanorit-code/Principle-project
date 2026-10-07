package com.example.costumerentalsystem.mapper;

import com.example.costumerentalsystem.domain.entity.Shipment;
import com.example.costumerentalsystem.dto.request.ShipmentRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShipmentMapper {

    public Shipment toEntity(ShipmentRequest request) {
        if (request == null) {
            return null;
        }
        Shipment shipment = new Shipment();
        shipment.setCourier(request.courier());
        shipment.setTrackingNo(request.trackingNo());
        shipment.setShippedAt(LocalDateTime.now());
        return shipment;
    }
}