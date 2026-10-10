package com.example.costumerentalsystem.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ShipmentRequest(
        @NotBlank(message = "กรุณาระบุบริษัทขนส่ง")
        String courier,

        @NotBlank(message = "กรุณาระบุเลขพัสดุ")
        String trackingNo) {
}
