package jung.api.webhook.infra;

import jung.api.webhook.dto.WebhookEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebhookProducer {

    private static final String TOPIC = "webhook-send";

    private final KafkaTemplate<String, WebhookEvent> kafkaTemplate;


    public void publish(WebhookEvent event) {
        kafkaTemplate.send(TOPIC,event);
    }
}
