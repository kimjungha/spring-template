package jung.api.webhook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jung.api.webhook.dto.WebhookEvent;
import jung.api.webhook.entity.WebhookLog;
import jung.api.webhook.infra.WebhookProducer;
import jung.api.webhook.repository.WebhookLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebhookService {

    @Value("${webhook.secret}")
    private String webhookSecret;

    @Value("${webhook.site}")
    private String webhookSite;

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

    private WebhookEvent createEvent() {
        return WebhookEvent.builder()
                .webhookUrl(webhookSite)
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
        try{
            String body = objectMapper.writeValueAsString(event);
            String signature = sign(body);

            webClient.post()
                    .uri(event.getWebhookUrl())
                    .header("X-Webhook-Secret", signature)
                    .header("X-Timestamp", String.valueOf(System.currentTimeMillis()))
                    .header("Idempotency-Key",UUID.randomUUID().toString()) // 멱등성 키
                    .bodyValue(body)
                    .retrieve()
                    .toBodilessEntity() // 응답코드만 받겠다
                    .doOnSuccess(v -> webhookStatusService.success(webhookLog.getId()))
                    .doOnError(e ->{
                        log.error("Webhook send failed: {}", event.getId(), e);
                        webhookStatusService.failure(webhookLog.getId());
                    })
                    .subscribe();
        } catch (JsonProcessingException e){
            log.error("Webhook JSON 변환 실패: {}", event.getId(), e);
        }

    }

    // HMAC-SHA256 서명 생성
    private String sign(String payload) {
        try{
            SecretKey secretKey = new SecretKeySpec(
                    webhookSecret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            byte[] hmac = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hmac);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

