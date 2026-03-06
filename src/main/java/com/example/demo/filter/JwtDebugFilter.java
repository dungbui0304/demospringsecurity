package com.example.demo.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.Jwt;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;
import lombok.RequiredArgsConstructor;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtDebugFilter extends OncePerRequestFilter {
    
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy token từ header Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("📍 Token nhận được từ client: {}", token);
            log.info("📍 Endpoint được gọi: {} {}", request.getMethod(), request.getRequestURI());
            
            try {
                // Decode token để lấy username
                Jwt jwt = jwtDecoder.decode(token);
                String username = jwt.getSubject();
                log.info("✅ Token hợp lệ - Username: {}", username);
                
                // Load user từ database
                userRepository.findByUsername(username).ifPresentOrElse(
                    user -> log.info("👤 User tìm thấy trong DB: {} (ID: {})", user.getUsername(), user.getId()),
                    () -> log.warn("⚠️ User không tìm thấy trong DB: {}", username)
                );
                
            } catch (Exception e) {
                log.error("❌ Token không hợp lệ: {}", e.getMessage());
            }
        } else {
            log.warn("❌ Không có token trong request: {} {}", request.getMethod(), request.getRequestURI());
        }

        // Tiếp tục xử lý request
        filterChain.doFilter(request, response);
    }
}