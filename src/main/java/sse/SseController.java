package sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class SseController {

    @Autowired
    private SseService sseService;

    @GetMapping("/subscribe/{clientId}")
    public SseEmitter subscribe(@PathVariable String clientId) {
        return sseService.subscribe(clientId);
    }

    // Endpoint to simulate sending an event
    @GetMapping("/send/{clientId}/{event}")
    public void sendEvent(@PathVariable String clientId, @PathVariable String event) {
        sseService.sendEvent(clientId, event);
    }
}
