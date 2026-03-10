package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.security.JwtService;
import com.example.demo.service.SseService;

import lombok.RequiredArgsConstructor;

import java.util.Random;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {
    private final SseService sseService;
    private final JwtService jwtService;

    @GetMapping("/subscribe")
    public SseEmitter subcribe(@RequestParam String userId) {
        SseEmitter emitter = sseService.subcribe(userId);
        return emitter;
    }
    
}
