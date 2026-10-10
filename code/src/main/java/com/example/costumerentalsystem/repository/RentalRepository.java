package com.example.costumerentalsystem.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.enums.RentalStatus;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByStatus(RentalStatus status);

    @EntityGraph(attributePaths = {"user", "costume", "payment", "shipment"})
    Page<Rental> findByStatus(RentalStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "costume", "payment", "shipment"})
    Page<Rental> findByUserId(Long userId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user", "costume", "payment", "shipment"})
    Page<Rental> findAll(Pageable pageable);

    boolean existsByCostumeId(Long costumeId);

    // เช็กว่าชุดนี้มีใบเช่าที่ยัง active และช่วงวันทับกับ [startDate, endDate] ไหม
    @Query("""
            select count(r) > 0 from Rental r
            where r.costume.id = :costumeId
              and r.status in :statuses
              and r.startDate <= :endDate
              and r.endDate >= :startDate
            """)
    boolean existsOverlap(@Param("costumeId") Long costumeId,
                          @Param("startDate") LocalDate startDate,
                          @Param("endDate") LocalDate endDate,
                          @Param("statuses") Collection<RentalStatus> statuses);
}