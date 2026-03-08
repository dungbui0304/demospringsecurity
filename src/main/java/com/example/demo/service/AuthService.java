package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
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

    public TokenResponse login(UserLogin request) {
        TokenResponse tokenResponse = new TokenResponse();
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        tokenResponse.setAccessToken(jwtService.generateToken(user.getUsername(), user.getRoles()));
        return tokenResponse;
    }
}
