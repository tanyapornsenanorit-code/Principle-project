package com.example.costumerentalsystem.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.dto.response.CostumeResponse;
import com.example.costumerentalsystem.exception.ResourceNotFoundException;
import com.example.costumerentalsystem.mapper.CostumeMapper;
import com.example.costumerentalsystem.repository.CostumeRepository;
import com.example.costumerentalsystem.repository.CostumeSpecifications;
import com.example.costumerentalsystem.service.CostumeQueryService;

@Service
@Transactional(readOnly = true)
public class CostumeQueryServiceImpl implements CostumeQueryService {

    private final CostumeRepository costumeRepository;
    private final CostumeMapper costumeMapper;

    public CostumeQueryServiceImpl(CostumeRepository costumeRepository, CostumeMapper costumeMapper) {
        this.costumeRepository = costumeRepository;
        this.costumeMapper = costumeMapper;
    }

    @Override
    public Page<CostumeResponse> search(String keyword, Long categoryId, CostumeStatus status, Pageable pageable) {
        Specification<Costume> spec = Specification
                .where(CostumeSpecifications.nameContains(keyword))
                .and(CostumeSpecifications.inCategory(categoryId))
                .and(CostumeSpecifications.hasStatus(status));
        return costumeRepository.findAll(spec, pageable).map(costumeMapper::toResponse);
    }

    @Override
    public CostumeResponse getById(Long id) {
        Costume costume = costumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ชุด", id));
        return costumeMapper.toResponse(costume);
    }
}
