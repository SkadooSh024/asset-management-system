package com.assetmanagement.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assetmanagement.backend.dto.LoginRequest;
import com.assetmanagement.backend.dto.LoginResponse;
import com.assetmanagement.backend.dto.LogoutRequest;
import com.assetmanagement.backend.dto.SimpleMessageResponse;
import com.assetmanagement.backend.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public SimpleMessageResponse logout(@Valid @RequestBody LogoutRequest request) {
        return authService.logout(request);
    }
}
