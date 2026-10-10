package com.example.costumerentalsystem.repository;

import org.springframework.data.jpa.domain.Specification;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;

// เงื่อนไขค้นหาชุดที่เอามาต่อกันได้ ถ้าไม่ส่งค่ามาจะคืน null (คือไม่กรอง)
// ใช้ Specification เพราะ query ที่เช็ก null ใน JPQL มีปัญหากับ PostgreSQL
public final class CostumeSpecifications {

    private CostumeSpecifications() {
    }

    public static Specification<Costume> nameContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        String pattern = "%" + keyword.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), pattern);
    }

    public static Specification<Costume> inCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Costume> hasStatus(CostumeStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
