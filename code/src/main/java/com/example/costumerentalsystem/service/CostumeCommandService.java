package com.example.costumerentalsystem.service;

import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.dto.request.CostumeRequest;
import com.example.costumerentalsystem.dto.response.CostumeResponse;

// ฝั่งเขียนของชุด (admin ใช้)
public interface CostumeCommandService {

    CostumeResponse create(CostumeRequest request);

    CostumeResponse update(Long id, CostumeRequest request);

    CostumeResponse changeStatus(Long id, CostumeStatus status);

    void delete(Long id);
}
