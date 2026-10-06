package com.example.costumerentalsystem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.costumerentalsystem.domain.entity.Category;
import com.example.costumerentalsystem.dto.request.CategoryRequest;
import com.example.costumerentalsystem.dto.response.CategoryResponse;
import com.example.costumerentalsystem.exception.ConflictException;
import com.example.costumerentalsystem.exception.ResourceNotFoundException;
import com.example.costumerentalsystem.mapper.CategoryMapper;
import com.example.costumerentalsystem.repository.CategoryRepository;
import com.example.costumerentalsystem.repository.CostumeRepository;
import com.example.costumerentalsystem.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CostumeRepository costumeRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CostumeRepository costumeRepository,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.costumeRepository = costumeRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        return categoryMapper.toResponse(find(id));
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new ConflictException("มีหมวดหมู่ชื่อนี้อยู่แล้ว");
        }
        Category category = new Category();
        categoryMapper.apply(category, request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = find(id);
        boolean renamed = !category.getName().equalsIgnoreCase(request.name().trim());
        if (renamed && categoryRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new ConflictException("มีหมวดหมู่ชื่อนี้อยู่แล้ว");
        }
        categoryMapper.apply(category, request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        Category category = find(id);
        if (costumeRepository.existsByCategoryId(id)) {
            throw new ConflictException("ลบไม่ได้ เพราะยังมีชุดอยู่ในหมวดหมู่นี้");
        }
        categoryRepository.delete(category);
    }

    private Category find(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("หมวดหมู่", id));
    }
}
