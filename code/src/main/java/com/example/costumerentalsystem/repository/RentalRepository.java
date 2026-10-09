package com.example.costumerentalsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.enums.RentalStatus;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByStatus(RentalStatus status);
}
