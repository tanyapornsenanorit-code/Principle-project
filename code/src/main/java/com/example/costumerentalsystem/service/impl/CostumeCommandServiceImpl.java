package com.example.costumerentalsystem.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.costumerentalsystem.domain.entity.Category;
import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.dto.request.CostumeRequest;
import com.example.costumerentalsystem.dto.response.CostumeResponse;
import com.example.costumerentalsystem.exception.ConflictException;
import com.example.costumerentalsystem.exception.ResourceNotFoundException;
import com.example.costumerentalsystem.mapper.CostumeMapper;
import com.example.costumerentalsystem.repository.CategoryRepository;
import com.example.costumerentalsystem.repository.CostumeRepository;
import com.example.costumerentalsystem.repository.RentalRepository;
import com.example.costumerentalsystem.service.CostumeCommandService;

@Service
@Transactional
public class CostumeCommandServiceImpl implements CostumeCommandService {

    private final CostumeRepository costumeRepository;
    private final CategoryRepository categoryRepository;
    private final RentalRepository rentalRepository;
    private final CostumeMapper costumeMapper;

    public CostumeCommandServiceImpl(CostumeRepository costumeRepository,
                                     CategoryRepository categoryRepository,
                                     RentalRepository rentalRepository,
                                     CostumeMapper costumeMapper) {
        this.costumeRepository = costumeRepository;
        this.categoryRepository = categoryRepository;
        this.rentalRepository = rentalRepository;
        this.costumeMapper = costumeMapper;
    }

    @Override
    public CostumeResponse create(CostumeRequest request) {
        Costume costume = new Costume();
        costumeMapper.apply(costume, request, findCategory(request.categoryId()));
        costume.setStatus(CostumeStatus.AVAILABLE);
        return costumeMapper.toResponse(costumeRepository.save(costume));
    }

    @Override
    public CostumeResponse update(Long id, CostumeRequest request) {
        Costume costume = find(id);
        costumeMapper.apply(costume, request, findCategory(request.categoryId()));
        return costumeMapper.toResponse(costumeRepository.save(costume));
    }

    @Override
    public CostumeResponse changeStatus(Long id, CostumeStatus status) {
        Costume costume = find(id);
        costume.setStatus(status);
        return costumeMapper.toResponse(costumeRepository.save(costume));
    }

    @Override
    public void delete(Long id) {
        Costume costume = find(id);
        if (rentalRepository.existsByCostumeId(id)) {
            throw new ConflictException("ลบไม่ได้ เพราะชุดนี้มีประวัติการเช่า (ให้เปลี่ยนสถานะเป็น \"ไม่ว่าง\" แทน)");
        }
        costumeRepository.delete(costume);
    }

    private Costume find(Long id) {
        return costumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ชุด", id));
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("หมวดหมู่", categoryId));
    }
}
