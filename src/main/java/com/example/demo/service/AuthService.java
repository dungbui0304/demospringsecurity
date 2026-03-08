package com.example.demo.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.UserLogin;
import com.example.demo.model.User;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public TokenResponse login(UserLogin request) {
        TokenResponse tokenResponse = new TokenResponse();
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        tokenResponse.setAccessToken(jwtService.generateToken(user.getUsername(), user.getRoles()));
        tokenResponse.setRefreshToken(jwtService.generateRefreshToken(user.getUsername()));
        return tokenResponse;
    }

    public TokenResponse refreshToken(String refreshToken) {
        TokenResponse response = new TokenResponse();
        Jwt jwt = jwtService.decodeJwt(refreshToken);

        if (!"refresh".equals(jwt.getClaim("type"))) {
            throw new RuntimeException("Invalid refresh token");
        }

        String userName = jwt.getSubject();
        User user = (User) userService.loadUserByUsername(userName);
        String newAccessToken = jwtService.generateToken(userName, user.getRoles());
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }
}
