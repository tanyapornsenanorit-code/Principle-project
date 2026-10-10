package com.example.costumerentalsystem.mapper;

import org.springframework.stereotype.Component;

import com.example.costumerentalsystem.domain.entity.Category;
import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.dto.request.CostumeRequest;
import com.example.costumerentalsystem.dto.response.CostumeResponse;

@Component
public class CostumeMapper {

    public CostumeResponse toResponse(Costume costume) {
        Category category = costume.getCategory(); // category เป็น null ได้
        return new CostumeResponse(
                costume.getId(),
                costume.getName(),
                category != null ? category.getId() : null,
                category != null ? category.getName() : null,
                costume.getPrice(),
                costume.getDescription(),
                costume.getStatus(),
                costume.getStatus().getDisplayName(),
                costume.getImageUrl());
    }

    // ไม่แตะ status เพราะ status เปลี่ยนตามขั้นตอนการเช่า
    public void apply(Costume costume, CostumeRequest request, Category category) {
        costume.setName(request.name().trim());
        costume.setCategory(category);
        costume.setPrice(request.pricePerDay());
        costume.setDescription(request.description());
        if (request.imageUrl() != null && !request.imageUrl().isBlank()) {
            costume.setImageUrl(request.imageUrl());
        }
    }
}
