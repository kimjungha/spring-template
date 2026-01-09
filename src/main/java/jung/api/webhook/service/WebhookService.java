package jung.api.webhook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jung.api.webhook.dto.WebhookEvent;
import jung.api.webhook.entity.WebhookLog;
import jung.api.webhook.infra.WebhookProducer;
import jung.api.webhook.repository.WebhookLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebhookService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private final WebhookProducer webhookProducer;
    private final WebhookLogRepository webhookLogRepository;
    private final WebhookStatusService webhookStatusService;

    /**
     * 비즈니스 흐름은 서비스 계층에 (어떤 정보를 담은 웹훅을 보낼거야)
     * 큐에 전송은 인프라 계층 producer 에서 책임진다.
     * 메서드에 흐름을 표시하고 private 메서드로 분리하여 각 메서드별 책임을 명확하게 한다.
     */
    @Transactional
    public void sendQueForWebhook() {
        log.info("Webhook service started");

        WebhookEvent event = createEvent();
        String payload = serialize(event);
        saveWebhookLog(event, payload);
        webhookProducer.publish(event);
    }

    private static WebhookEvent createEvent() {
        return WebhookEvent.builder()
                .webhookUrl("https://webhook.site/8bb16cbc-20bd-4677-b851-6417ddb24a19")
                .id(UUID.randomUUID().toString())
                .retryCount(0)
                .build();
    }

    private void saveWebhookLog(WebhookEvent event, String payload) {
        WebhookLog webhookLog = WebhookLog.builder()
                .eventType("webhook-send")
                .webhookUrl(event.getWebhookUrl())
                .webhookId(event.getId())
                .payload(payload)
                .status("PENDING") //todo : enum 처리
                .build();
        webhookLogRepository.save(webhookLog);
    }

    /**
     * 비동기 webClient 사용
     */
    public void sendWebhook(WebhookEvent event) {
        log.info("WebClient send Webhook:{}",event.getId());

        WebhookLog webhookLog = webhookLogRepository.findByWebhookId(event.getId());
        if (webhookLog == null || Objects.equals(webhookLog.getStatus(), "SUCCESS")) {
            return;
        }
        webClient.post()
                .uri(event.getWebhookUrl())
                .bodyValue(event)
                .retrieve()
                .toBodilessEntity() // 응답코드만 받겠다
                .doOnSuccess(v -> webhookStatusService.success(webhookLog.getId()))
                .doOnError(e ->{
                    log.error("Webhook send failed: {}", event.getId(), e);
                    webhookStatusService.failure(webhookLog.getId());
                        })
                .subscribe();
    }

    /*
    * WebhookEvent convert to String
    */
    private String serialize(WebhookEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

