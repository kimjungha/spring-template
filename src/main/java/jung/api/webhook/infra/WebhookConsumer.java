package jung.api.webhook.infra;

import jung.api.webhook.dto.WebhookEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebhookConsumer {

    @KafkaListener(
            topics = "webhook-send",
            groupId = "webhook-consumer"
    )
    public void consume(WebhookEvent event) {
        System.out.println(event.getId()+"소비합니다.");

    }
}
