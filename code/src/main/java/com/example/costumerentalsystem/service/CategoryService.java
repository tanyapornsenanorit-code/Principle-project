package com.example.costumerentalsystem.service;

import java.util.List;

import com.example.costumerentalsystem.dto.request.CategoryRequest;
import com.example.costumerentalsystem.dto.response.CategoryResponse;

public interface CategoryService {

    List<CategoryResponse> findAll();

    CategoryResponse getById(Long id);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}
