package com.example.costumerentalsystem.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.dto.response.CostumeResponse;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.repository.CostumeRepository;

@RestController
@RequestMapping("/api/v1/costumes")
public class CostumeApiController {

    private final CostumeRepository costumeRepository;

    public CostumeApiController(CostumeRepository costumeRepository) {
        this.costumeRepository = costumeRepository;
    }

    // GET /api/costumes
    // ดึงข้อมูลชุดทั้งหมด
    @GetMapping
    public ResponseEntity<List<CostumeResponse>> getAllCostumes() {
        return ResponseEntity.ok(costumeRepository.findAll().stream().map(this::toResponse).toList());
    }

    // GET /api/costumes/{id}
    // ดึงข้อมูลชุดตาม ID
    @GetMapping("/{id}")
    public ResponseEntity<CostumeResponse> getCostumeById(
            @PathVariable Long id) {

        return costumeRepository.findById(id)
                .map(this::toResponse).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/costumes/search?keyword=...
    // ค้นหาจากชื่อชุดหรือชื่อหมวดหมู่
    @GetMapping("/search")
    public ResponseEntity<List<CostumeResponse>> searchCostumes(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                costumeRepository.searchByNameOrCategory(keyword).stream().map(this::toResponse).toList()
        );
    }

    // GET /api/costumes/status/{status}
    // ค้นหาชุดตามสถานะ
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CostumeResponse>> getCostumesByStatus(
            @PathVariable CostumeStatus status) {

        return ResponseEntity.ok(
                costumeRepository.findByStatus(status).stream().map(this::toResponse).toList()
        );
    }

    private CostumeResponse toResponse(Costume costume) {
        Long categoryId = costume.getCategory() == null ? null : costume.getCategory().getId();
        String categoryName = costume.getCategory() == null ? null : costume.getCategory().getName();
        return new CostumeResponse(costume.getId(), costume.getName(), categoryId, categoryName,
                costume.getPrice(), costume.getDescription(), costume.getStatus(),
                costume.getStatus() == null ? null : costume.getStatus().name(), costume.getImageUrl());
    }
}