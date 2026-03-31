package jung.api.payment;

import jung.dto.SseEvent;
import jung.infrastructure.SseRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final SseRegistry sseRegistry;

    public void confirmPayment(String clientId) {
        // ... 결제 확인 로직 구현

       // 완료 이벤트 푸시 -> 연결 종료
        sseRegistry.sendAndClose(clientId, "payment-complete",
                new SseEvent("complete", Map.of(
                        "orderId",       "ORDER_123",
                        "amount", BigDecimal.valueOf(10000.00)
                ))
        );
    }
}
