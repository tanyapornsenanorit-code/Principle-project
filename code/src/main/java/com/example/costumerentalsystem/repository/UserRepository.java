package com.example.costumerentalsystem.repository;

import java.util.Optional; // 🟢 เพิ่ม import นี้

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.costumerentalsystem.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByUsername(String username);
    
    // 🟢 เพิ่มบรรทัดนี้เพื่อแก้ไขขีดแดงใน UserService.java
    Optional<User> findByEmail(String email);
}