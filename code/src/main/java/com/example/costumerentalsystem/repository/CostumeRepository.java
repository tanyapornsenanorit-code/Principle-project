package com.example.costumerentalsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;

@Repository
public interface CostumeRepository extends JpaRepository<Costume, Long> {

    List<Costume> findByStatus(CostumeStatus status);

    List<Costume> findByNameContainingIgnoreCase(String name);

    /** ค้นหาจากชื่อชุดหรือชื่อหมวดหมู่ (category เป็น Entity แล้ว จึงเขียน derived query ตรง ๆ ไม่ได้) */
    @Query("""
           select c from Costume c
           left join c.category cat
           where lower(c.name) like lower(concat('%', :keyword, '%'))
              or lower(cat.name) like lower(concat('%', :keyword, '%'))
           """)
    List<Costume> searchByNameOrCategory(@Param("keyword") String keyword);
}
