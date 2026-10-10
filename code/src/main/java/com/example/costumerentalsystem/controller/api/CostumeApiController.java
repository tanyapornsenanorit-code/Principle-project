package com.example.costumerentalsystem.controller.api;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.dto.request.CostumeRequest;
import com.example.costumerentalsystem.dto.response.CostumeResponse;
import com.example.costumerentalsystem.service.CostumeCommandService;
import com.example.costumerentalsystem.service.CostumeQueryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/costumes")
public class CostumeApiController {

    private final CostumeCommandService commandService;
    private final CostumeQueryService queryService;

    public CostumeApiController(
            CostumeCommandService commandService,
            CostumeQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    // GET /api/v1/costumes?page=0&size=10&sort=name,asc
    @GetMapping
    public ResponseEntity<Page<CostumeResponse>> getAllCostumes(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) CostumeStatus status,
            @PageableDefault(
                    size = 10,
                    sort = "name",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {

        return ResponseEntity.ok(
                queryService.search(keyword, categoryId, status, pageable)
        );
    }

    // GET /api/v1/costumes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CostumeResponse> getCostumeById(
            @PathVariable Long id) {
        return ResponseEntity.ok(queryService.getById(id));
    }

    // GET /api/v1/costumes/search?keyword=dress&page=0&size=10
    @GetMapping("/search")
    public ResponseEntity<Page<CostumeResponse>> searchCostumes(
            @RequestParam String keyword,
            @PageableDefault(
                    size = 10,
                    sort = "name",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {

        return ResponseEntity.ok(
                queryService.search(keyword, null, null, pageable)
        );
    }

    // GET /api/v1/costumes/status/AVAILABLE?page=0&size=10
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<CostumeResponse>> getCostumesByStatus(
            @PathVariable CostumeStatus status,
            @PageableDefault(
                    size = 10,
                    sort = "name",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {

        return ResponseEntity.ok(
                queryService.search(null, null, status, pageable)
        );
    }

    // POST /api/v1/costumes
    @PostMapping
    public ResponseEntity<CostumeResponse> createCostume(
            @Valid @RequestBody CostumeRequest request) {

        CostumeResponse created = commandService.create(request);

        return ResponseEntity
                .created(URI.create("/api/v1/costumes/" + created.id()))
                .body(created);
    }

    // PUT /api/v1/costumes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CostumeResponse> updateCostume(
            @PathVariable Long id,
            @Valid @RequestBody CostumeRequest request) {

        return ResponseEntity.ok(commandService.update(id, request));
    }

    // PATCH /api/v1/costumes/{id}/status?status=AVAILABLE
    @PatchMapping("/{id}/status")
    public ResponseEntity<CostumeResponse> changeCostumeStatus(
            @PathVariable Long id,
            @RequestParam CostumeStatus status) {

        return ResponseEntity.ok(commandService.changeStatus(id, status));
    }

    // DELETE /api/v1/costumes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostume(
            @PathVariable Long id) {

        commandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
