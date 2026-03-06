package com.example.demo.controller;

import java.time.Instant;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.UserLogin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody UserLogin request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Instant now = Instant.now();
            String token = jwtEncoder.encode(JwtEncoderParameters.from(
                JwtClaimsSet.builder()
                .subject(request.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("scope", "read write")
                .build())
            ).getTokenValue();
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setAccessToken(token);
            return ResponseEntity.ok(new ApiResponse<>(tokenResponse, "Login successful"));

            
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(401).body(new ApiResponse<>(null, "Invalid username or password"));
        }

        // return ResponseEntity.ok(new ApiResponse<>(tokenResponse, "Login successful"));
    }
}
