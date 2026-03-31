package jung.api;

import jung.infrastructure.SseRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
@Slf4j
public class SseController {

    private final SseRegistry sseRegistry;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() {

        String clientId = UUID.randomUUID().toString();
        SseEmitter emitter = sseRegistry.register(clientId);
        log.info("클라이언트 연결: {}", emitter);

        try {
            emitter.send(
                    SseEmitter.event()
                            .name("connection")
                            .data(clientId + " connected")
                            .id(clientId)
            );
        }catch (Exception e){
            log.error("SSE 연결 이벤트 전송 실패: clientId={}, error={}", clientId, e.getMessage());
            emitter.complete();
        }
        log.info("SSE 구독 시작: {}", clientId);
        return emitter;
    }
}
