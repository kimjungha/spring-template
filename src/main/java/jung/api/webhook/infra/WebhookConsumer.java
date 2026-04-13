package jung.api.webhook.infra;

import jung.api.webhook.service.WebhookService;
import jung.api.webhook.dto.WebhookEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookConsumer {

    private final WebhookService webhookService;

    @KafkaListener(
            topics = "webhook-send",
            groupId = "webhook-consumer"
    )
    public void consume(WebhookEvent event) {
        log.info("{}소비합니다.", event.getId());
        webhookService.sendWebhook(event);
    }
}
