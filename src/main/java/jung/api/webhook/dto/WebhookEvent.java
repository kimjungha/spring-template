package jung.api.webhook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebhookEvent {
    private String webhookUrl;
    private String id;
    private int retryCount;
}
