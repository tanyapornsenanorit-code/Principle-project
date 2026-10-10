package com.example.costumerentalsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.costumerentalsystem.dto.request.CategoryRequest;
import com.example.costumerentalsystem.dto.response.CategoryResponse;

public interface CategoryService {

    // สำหรับหน้าเว็บเดิม
    List<CategoryResponse> findAll();

    // สำหรับ REST API ที่รองรับ Pagination
    Page<CategoryResponse> findAll(Pageable pageable);

    CategoryResponse getById(Long id);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}
