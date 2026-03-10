package com.example.demo.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.service.SseService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RandomDataScheduler {
    private final SseService sseService;

    @Scheduled(fixedRate = 3000)
    public void sendRandom() {

        sseService.startFakeServerPush();
    }
}
