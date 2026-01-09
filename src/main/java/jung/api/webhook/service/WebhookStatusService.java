package jung.api.webhook.service;

import jung.api.webhook.entity.WebhookLog;
import jung.api.webhook.repository.WebhookLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WebhookStatusService {
    private final WebhookLogRepository webhookLogRepository;

    /*
    * WebhookService 에서 처리하면 Self-invocation 문제가 발생할 수 있다. 그래서 서비스 분리
    */
    @Transactional
    public void success(Long id) {
        WebhookLog webhookLog = webhookLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WebhookLog not found: " + id));
        webhookLog.success();
    }

    @Transactional
    public void failure(Long id) {
        WebhookLog webhookLog = webhookLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WebhookLog not found: " + id));
        webhookLog.failure();
    }
}
