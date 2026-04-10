package com.assetmanagement.backend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assetmanagement.backend.dto.LoginRequest;
import com.assetmanagement.backend.entity.User;
import com.assetmanagement.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173"})
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Sai tài khoản hoặc mật khẩu");
            return response;
        }

        User user = userOpt.get();

        response.put("success", true);
        response.put("message", "Đăng nhập thành công");
        response.put("user", Map.of(
            "userId", user.getUserId(),
            "username", user.getUsername(),
            "fullName", user.getFullName(),
            "email", user.getEmail(),
            "status", user.getStatus()
        ));

        return response;
    }
}