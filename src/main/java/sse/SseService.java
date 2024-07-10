package sse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {

    private static final long TIMEOUT = 1800000L; // 30 minutes

    private final Map<String, SseEmitter> clients = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String clientId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitter.onCompletion(() -> clients.remove(clientId));
        emitter.onTimeout(() -> clients.remove(clientId));
        emitter.onError((e) -> clients.remove(clientId));
        clients.put(clientId, emitter);
        return emitter;
    }

    public void sendEvent(String clientId, String event) {
        SseEmitter emitter = clients.get(clientId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("message").data(event));
            } catch (IOException e) {
                clients.remove(clientId);
            }
        }
    }
}

