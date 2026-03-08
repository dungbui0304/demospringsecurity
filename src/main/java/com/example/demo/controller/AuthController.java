package com.example.demo.controller;

import java.lang.annotation.Retention;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.UserLogin;
import com.example.demo.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody UserLogin request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseEntity.ok(
            new ApiResponse<>(tokenResponse, "Login successful"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        TokenResponse tokenResponse = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(
            new ApiResponse<>(tokenResponse, "Refresh token successfully"));
    }
    

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok("Current user: " + username);
    }

    @GetMapping("/profile")
    public ResponseEntity<@Nullable Object> getProfile(Authentication authentication) {
        return ResponseEntity.ok(authentication.getPrincipal());
    }


    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(Authentication authentication) {
        System.out.println(authentication.getAuthorities());
        return ResponseEntity.ok(authentication.getAuthorities());
    }

    @GetMapping("/debug")
    public Object debug(Authentication authentication) {
        return authentication;
    }
}