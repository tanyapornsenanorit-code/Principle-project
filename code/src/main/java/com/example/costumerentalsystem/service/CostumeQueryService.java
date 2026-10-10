package com.example.costumerentalsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.dto.response.CostumeResponse;

// ฝั่งอ่านของชุด แยกออกมาเพื่อให้คนที่แค่ดูรายการไม่ต้องรู้จักเมธอดแก้ไข (ISP)
public interface CostumeQueryService {

    Page<CostumeResponse> search(String keyword, Long categoryId, CostumeStatus status, Pageable pageable);

    CostumeResponse getById(Long id);
}
