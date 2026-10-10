package com.example.costumerentalsystem.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.dto.response.CostumeResponse;
import com.example.costumerentalsystem.mapper.CostumeMapper;
import com.example.costumerentalsystem.repository.CostumeRepository;

@RestController
@RequestMapping("/api/v1/costumes")
public class CostumeApiController {

    private final CostumeRepository costumeRepository;
    private final CostumeMapper costumeMapper;

    public CostumeApiController(
            CostumeRepository costumeRepository,
            CostumeMapper costumeMapper) {
        this.costumeRepository = costumeRepository;
        this.costumeMapper = costumeMapper;
    }

    // GET /api/v1/costumes
    @GetMapping
    public ResponseEntity<List<CostumeResponse>> getAllCostumes() {
        return ResponseEntity.ok(
                costumeRepository.findAll()
                        .stream()
                        .map(costumeMapper::toResponse)
                        .toList()
        );
    }

    // GET /api/v1/costumes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CostumeResponse> getCostumeById(
            @PathVariable Long id) {
        return costumeRepository.findById(id)
                .map(costumeMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/v1/costumes/search?keyword=...
    @GetMapping("/search")
    public ResponseEntity<List<CostumeResponse>> searchCostumes(
            @RequestParam String keyword) {
        return ResponseEntity.ok(
                costumeRepository.searchByNameOrCategory(keyword)
                        .stream()
                        .map(costumeMapper::toResponse)
                        .toList()
        );
    }

    // GET /api/v1/costumes/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CostumeResponse>> getCostumesByStatus(
            @PathVariable CostumeStatus status) {
        return ResponseEntity.ok(
                costumeRepository.findByStatus(status)
                        .stream()
                        .map(costumeMapper::toResponse)
                        .toList()
        );
    }
}
