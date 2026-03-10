package com.example.demo.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseService {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subcribe(String userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> emitters.remove(userId));
        
        return emitter;
    }

    public void send(String userId, String message) {
        SseEmitter emitter = emitters.get(userId);

        if (emitter == null) {
            System.out.println("No emitter for user: " + userId);
            return;
        }

        try {
             System.out.println("Send SSE to " + userId + ": " + message);
            emitter.send(
                SseEmitter.event()
                    .name("data")
                    .data(message)
            );
        } catch (IOException e) {
            emitters.remove(userId);
        }
    }

    public void startFakeServerPush() {
        int count = 1;

        while (true) {
            try {
                Thread.sleep(3000);
                int currentCount = count;

                emitters.keySet().forEach(userId -> {
                    if (currentCount == 10) {
                        send(userId, "DONE");
                    } else {
                        send(userId, "Message #" + currentCount + " from server");
                    }
                });
                if (currentCount == 10) {
                    break;
                }
                count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
