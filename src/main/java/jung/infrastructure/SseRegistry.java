package jung.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSE 관리 레지스트리
 */

@Component
@Slf4j
public class SseRegistry {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(String clientId) {

        SseEmitter emitter = new SseEmitter(300_000L); // 5분 타임아웃

        // callback 등록 - 클라이언트 연결 종료, 타임아웃, 에러 발생 시 레지스트리에서 제거
        emitter.onCompletion(() -> {
            emitters.remove(clientId);
            log.info("SSE 완료: clientId={}", clientId);
        });

        emitter.onTimeout(() -> {
            emitters.remove(clientId);
            log.info("SSE 타임아웃: clientId={}", clientId);
        });

        emitter.onError(e -> {
            emitters.remove(clientId);
            log.error("SSE 에러: clientId={}, error={}", clientId, e.getMessage());
        });

        // 마지막으로 레지스트리에 등록
        emitters.put(clientId, emitter);
        log.info("SSE 등록: clientId={}", clientId);
        return emitter;
    }
    //  이벤트 전송 (연결 유지)
    public void send(String sessionId, String eventName, Object data) {
        SseEmitter emitter = emitters.get(sessionId);
        if (emitter == null) {
            log.warn("SSE emitter 없음: {}", sessionId);
            return;
        }
        try {
            emitter.send(
                    SseEmitter.event()
                            .name(eventName)
                            .data(data)
            );
        } catch (IOException e) {
            emitters.remove(sessionId);
            log.warn("SSE 전송 실패: {}", sessionId);
        }
    }
    // 이벤트 전송 후 연결 종료 (결제 완료처럼 1회성)
    public void sendAndClose(String sessionId, String eventName, Object data) {
        SseEmitter emitter = emitters.get(sessionId);
        if (emitter == null) return;
        try {
            emitter.send(
                    SseEmitter.event()
                            .name(eventName)
                            .data(data)
            );
            emitter.complete(); // 전송 후 연결 종료
        } catch (IOException e) {
            log.warn("SSE 전송 실패: {}", sessionId);
        } finally {
            emitters.remove(sessionId);
        }
    }
}


