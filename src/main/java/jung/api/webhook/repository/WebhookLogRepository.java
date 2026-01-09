package jung.api.webhook.repository;

import jung.api.webhook.entity.WebhookLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WebhookLogRepository extends JpaRepository<WebhookLog, Long> {
    WebhookLog findByWebhookId(String webhookId);
    List<WebhookLog> findByStatusAndRetryCountLessThanAndNextRetryAtLessThanEqual(String status, int retryCount, LocalDateTime now);
}
