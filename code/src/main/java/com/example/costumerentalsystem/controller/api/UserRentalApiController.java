package com.example.costumerentalsystem.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.costumerentalsystem.dto.response.RentalResponse;
import com.example.costumerentalsystem.service.RentalService;
import com.example.costumerentalsystem.service.UserAccountService;

@RestController
@RequestMapping("/api/v1/users")
public class UserRentalApiController {

    private final RentalService rentalService;
    private final UserAccountService userAccountService;

    public UserRentalApiController(
            RentalService rentalService,
            UserAccountService userAccountService) {
        this.rentalService = rentalService;
        this.userAccountService = userAccountService;
    }

    // GET /api/v1/users/{id}/rentals?page=0&size=10&sort=createdAt,desc
    @GetMapping("/{id}/rentals")
    public ResponseEntity<Page<RentalResponse>> getUserRentals(
            @PathVariable Long id,
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        // ตรวจสอบก่อนว่ามีผู้ใช้งาน ID นี้จริง
        userAccountService.getById(id);

        // ใช้ Service เดิมและรองรับ Pagination/Sorting
        return ResponseEntity.ok(
                rentalService.findByUser(id, pageable)
        );
    }
}
