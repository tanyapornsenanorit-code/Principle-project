package com.example.costumerentalsystem.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.repository.CostumeRepository;

@RestController
@RequestMapping("/api/costumes")
public class CostumeApiController {

    private final CostumeRepository costumeRepository;

    public CostumeApiController(CostumeRepository costumeRepository) {
        this.costumeRepository = costumeRepository;
    }

    // GET /api/costumes
    // ดึงข้อมูลชุดทั้งหมด
    @GetMapping
    public ResponseEntity<List<Costume>> getAllCostumes() {
        return ResponseEntity.ok(costumeRepository.findAll());
    }

    // GET /api/costumes/{id}
    // ดึงข้อมูลชุดตาม ID
    @GetMapping("/{id}")
    public ResponseEntity<Costume> getCostumeById(
            @PathVariable Long id) {

        return costumeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/costumes/search?keyword=...
    // ค้นหาจากชื่อชุดหรือชื่อหมวดหมู่
    @GetMapping("/search")
    public ResponseEntity<List<Costume>> searchCostumes(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                costumeRepository.searchByNameOrCategory(keyword)
        );
    }

    // GET /api/costumes/status/{status}
    // ค้นหาชุดตามสถานะ
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Costume>> getCostumesByStatus(
            @PathVariable CostumeStatus status) {

        return ResponseEntity.ok(
                costumeRepository.findByStatus(status)
        );
    }
}