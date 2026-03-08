package com.example.demo.security;

import java.time.Instant;
import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String generateToken(String userName, List<String> roles) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(userName)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .claim("scopes", roles)
            .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims))
            .getTokenValue();
    }

    public Jwt decodeJwt(String token) {
        return jwtDecoder.decode(token);
    }
}
