package com.assetmanagement.backend.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assetmanagement.backend.dto.LoginRequest;
import com.assetmanagement.backend.dto.LoginResponse;
import com.assetmanagement.backend.dto.LogoutRequest;
import com.assetmanagement.backend.dto.SimpleMessageResponse;
import com.assetmanagement.backend.entity.User;
import com.assetmanagement.backend.exception.BusinessException;
import com.assetmanagement.backend.repository.UserRepository;
import com.assetmanagement.backend.util.ResponseMapper;
import com.assetmanagement.backend.util.SystemCodes;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "Sai tai khoan hoac mat khau."));

        if (!SystemCodes.USER_STATUS_ACTIVE.equalsIgnoreCase(user.getStatus())) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Tai khoan hien khong the dang nhap.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Sai tai khoan hoac mat khau.");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return LoginResponse.builder()
            .success(true)
            .message("Dang nhap thanh cong.")
            .user(ResponseMapper.toUserSession(user))
            .build();
    }

    @Transactional(readOnly = true)
    public SimpleMessageResponse logout(LogoutRequest request) {
        if (request != null && request.getUsername() != null) {
            userRepository.findByUsername(request.getUsername());
        }

        return SimpleMessageResponse.builder()
            .success(true)
            .message("Dang xuat thanh cong.")
            .build();
    }
}
