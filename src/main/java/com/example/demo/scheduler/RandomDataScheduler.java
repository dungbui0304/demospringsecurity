package com.example.demo.scheduler;

import java.util.Random;

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

        int random = new Random().nextInt(100);
        sseService.send("dungbt5", "Random: " + random);
    }
}
