package com.example.daobe.notification.domain;

import java.io.IOException;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
public class NotificationEmitter {

    private static final String DELIMITER = "_";
    private static final String NOTIFICATION_EVENT_NAME = "NOTIFICATION_EVENT";

    private final Long userId;
    private final String emitterId;
    private final SseEmitter emitter;

    @Builder
    public NotificationEmitter(Long userId, SseEmitter emitter) {
        this.userId = userId;
        this.emitterId = generateEmitterId(userId);
        this.emitter = emitter;
    }

    private String generateEmitterId(Long userId) {
        return userId + DELIMITER + System.currentTimeMillis();
    }

    public void sendToClient(Object data) throws IOException {
        emitter.send(SseEmitter.event()
                .id(generateEventId())
                .name(NOTIFICATION_EVENT_NAME)
                .data(data)
        );
    }

    private String generateEventId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
