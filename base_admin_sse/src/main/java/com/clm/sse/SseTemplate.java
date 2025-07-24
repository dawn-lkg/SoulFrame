package com.clm.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈黎明
 * @since 2025-03-08
 */
@Component
@Slf4j
public class SseTemplate {

    private final SseProperties props;

    private final ConcurrentMap<String, SseEmitter> repo = new ConcurrentHashMap<>();

    public SseTemplate(SseProperties props) {
        this.props = props;
        startHeartbeat();
    }

    public SseEmitter connect(String clientId) {
        return connect(clientId, props.getDefaultTimeout());
    }

    public SseEmitter connect(String businessType, String clientId, Duration timeout) {
        return connect(businessType + ":" + clientId, timeout);
    }

    public SseEmitter connect(String businessType, String clientId) {
        return connect(businessType + ":" + clientId);
    }

    public SseEmitter connect(String clientId, Duration timeout) {
        Long ms = timeout.isZero() ? 0 : timeout.toMillis();
        log.warn("SSE client connected: {},timeOut:{}", clientId, ms);
        SseEmitter emitter = new SseEmitter(ms);
        repo.put(clientId, emitter);

        // 清理
        emitter.onCompletion(() -> {
            log.warn("SSE client disconnected: {}", clientId);
            repo.remove(clientId);
        });
        emitter.onTimeout(() -> {

            repo.remove(clientId);
            emitter.complete();
            log.warn("SSE client timeout: {}", clientId);
        });
        emitter.onError(e -> {
            log.warn("SSE send failed to client: {}, error: {}", clientId, e.getMessage());
            emitter.complete(); // 主动关闭连接
            repo.values().remove(emitter); // 从注册表中移除
        });

        return emitter;
    }

    /**
     * 单发
     */
    public void send(String clientId, Object data) {
        send(clientId, "message", data);
    }

    public void send(String clientId, String event, Object data) {
        send0(repo.get(clientId), event, data);
    }

    public void sendTo(Collection<String> ids, String event, Object data) {
        ids.stream()
                .map(repo::get)
                .filter(Objects::nonNull)
                .forEach(e -> send0(e, event, data));
    }

    public void broadcast(String event, Object data) {
        sendTo(repo.keySet(), event, data);
    }

    public void broadcast(String businessType, String event, Object data) {
        repo.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(businessType))
                .map(Map.Entry::getValue)
                .filter(Objects::nonNull)
                .forEach(e -> send0(e, event, data));
    }

    private void send0(SseEmitter emitter, String event, Object data) {
        if (emitter == null) return;
        try {
            emitter.send(SseEmitter.event()
                    .name(event)
                    .data(data)
                    .id(UUID.randomUUID().toString()));
        } catch (IOException e) {
            log.warn("Send failed: {}", e.getMessage());
            // 重试一次
            if (props.getMaxRetry() > 0) {
                repo.values().remove(emitter);
            }
        }
    }

    private void startHeartbeat() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(
                        () -> broadcast("heartbeat", Map.of("ts", System.currentTimeMillis())),
                        props.getHeartbeat().toMillis(),
                        props.getHeartbeat().toMillis(),
                        TimeUnit.MILLISECONDS
                );
    }
}
