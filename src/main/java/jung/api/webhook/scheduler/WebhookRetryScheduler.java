package jung.api.webhook.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jung.api.webhook.service.WebhookService;
import jung.api.webhook.dto.WebhookEvent;
import jung.api.webhook.entity.WebhookLog;
import jung.api.webhook.repository.WebhookLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookRetryScheduler {

    private static final int MAX_RETRY_COUNT = 7;
    private final ObjectMapper objectMapper;

    private final WebhookLogRepository webhookLogRepository;
    private final WebhookService webhookService;

    @Scheduled(fixedDelay = 30_000) // 30 초마다
    public void retryWebhook(){
        LocalDateTime now = LocalDateTime.now();
        log.info("{} 시간 기준 스케줄링 실행",now);
        List<WebhookLog> targets = webhookLogRepository.findByStatusAndRetryCountLessThanAndNextRetryAtLessThanEqual(
                "RETRY", MAX_RETRY_COUNT,now
        );

        if(targets.isEmpty()){
            log.info("{} 시간 기준 재전송 타겟 None",now);
            return;
        }

        log.info("현재시간 {} 기준 : RetryWebhook Size :{}",now,targets.size());
        for(WebhookLog webhookLog : targets){
            //string convert to WebhookEvent
            WebhookEvent event = deserialize(webhookLog.getPayload());
            webhookService.sendWebhook(event);
        }

    }
    /*
    * string convert to WebhookEvent
    */
    private WebhookEvent deserialize(String payload) {
        try {
            return objectMapper.readValue(payload, WebhookEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
