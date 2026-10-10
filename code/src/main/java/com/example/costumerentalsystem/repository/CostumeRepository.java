package com.example.costumerentalsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;

import jakarta.persistence.LockModeType;

@Repository
public interface CostumeRepository extends JpaRepository<Costume, Long>, JpaSpecificationExecutor<Costume> {

    List<Costume> findByStatus(CostumeStatus status);

    List<Costume> findByNameContainingIgnoreCase(String name);

    // ค้นจากชื่อชุดหรือชื่อหมวด (CostumeService เก่ายังใช้อยู่)
    @Query("""
           select c from Costume c
           left join c.category cat
           where lower(c.name) like lower(concat('%', :keyword, '%'))
              or lower(cat.name) like lower(concat('%', :keyword, '%'))
           """)
    List<Costume> searchByNameOrCategory(@Param("keyword") String keyword);

    boolean existsByCategoryId(Long categoryId);

    // ล็อกแถวชุดตอนสร้างใบเช่า กันสองคนจองชุดเดียวกันพร้อมกัน
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Costume c where c.id = :id")
    Optional<Costume> findByIdForUpdate(@Param("id") Long id);

    // ดึง category มาพร้อมกันในคำสั่งเดียว กัน N+1 ตอนแสดงรายการแบบแบ่งหน้า
    @Override
    @EntityGraph(attributePaths = "category")
    Page<Costume> findAll(Specification<Costume> spec, Pageable pageable);
}