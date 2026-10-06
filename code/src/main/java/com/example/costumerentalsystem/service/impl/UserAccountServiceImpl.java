package com.example.costumerentalsystem.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.domain.entity.UserProfile;
import com.example.costumerentalsystem.domain.enums.Role;
import com.example.costumerentalsystem.dto.request.RegisterRequest;
import com.example.costumerentalsystem.dto.request.UserProfileRequest;
import com.example.costumerentalsystem.dto.response.UserResponse;
import com.example.costumerentalsystem.exception.ConflictException;
import com.example.costumerentalsystem.exception.ResourceNotFoundException;
import com.example.costumerentalsystem.mapper.UserMapper;
import com.example.costumerentalsystem.repository.UserRepository;
import com.example.costumerentalsystem.service.UserAccountService;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserAccountServiceImpl(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder,
                                  UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("ชื่อผู้ใช้นี้มีอยู่ในระบบแล้ว");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("อีเมลนี้ถูกใช้งานแล้ว");
        }

        // สมัครใหม่ได้แค่ role USER
        User user = new User(request.username(), passwordEncoder.encode(request.password()), Role.USER);
        user.setEmail(request.email());

        UserProfile profile = new UserProfile();
        profile.setFullName(request.fullName());
        profile.setPhone(request.phone());
        user.setProfile(profile);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long userId) {
        return userMapper.toResponse(findById(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {
        return userMapper.toResponse(locateByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("ผู้ใช้ " + username + " ไม่พบในระบบ")));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findByEmailOrUsername(String identifier) {
        return userMapper.toResponse(locate(identifier));
    }

    @Override
    public UserResponse updateProfile(Long userId, UserProfileRequest request) {
        User user = findById(userId);

        boolean emailChanged = !request.email().equalsIgnoreCase(user.getEmail());
        if (emailChanged && userRepository.existsByEmail(request.email())) {
            throw new ConflictException("อีเมลนี้ถูกใช้งานแล้ว");
        }
        user.setEmail(request.email());

        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = new UserProfile();
            user.setProfile(profile);
        }
        userMapper.apply(profile, request);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void changePassword(String identifier, String newRawPassword) {
        User user = locate(identifier);
        user.setPassword(passwordEncoder.encode(newRawPassword));
        userRepository.save(user);
    }

    private User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ผู้ใช้", userId));
    }

    // findByUsername ของ repository คืน null ถ้าไม่เจอ (controller เก่ายังใช้) เลยห่อ Optional ตรงนี้
    private Optional<User> locateByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    private User locate(String identifier) {
        return userRepository.findByEmail(identifier)
                .or(() -> locateByUsername(identifier))
                .orElseThrow(() -> new ResourceNotFoundException("ผู้ใช้ " + identifier + " ไม่พบในระบบ"));
    }
}
