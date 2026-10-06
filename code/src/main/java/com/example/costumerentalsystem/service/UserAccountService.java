package com.example.costumerentalsystem.service;

import com.example.costumerentalsystem.dto.request.RegisterRequest;
import com.example.costumerentalsystem.dto.request.UserProfileRequest;
import com.example.costumerentalsystem.dto.response.UserResponse;

public interface UserAccountService {

    UserResponse register(RegisterRequest request);

    UserResponse getById(Long userId);

    UserResponse getByUsername(String username);

    // ใช้ตอนลืมรหัสผ่าน หาจากอีเมลก่อน ไม่เจอค่อยหาจาก username
    UserResponse findByEmailOrUsername(String identifier);

    UserResponse updateProfile(Long userId, UserProfileRequest request);

    void changePassword(String identifier, String newRawPassword);
}
