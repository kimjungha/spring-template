package jung.api.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/confirm/{clientId}")
    public ResponseEntity<Object> confirmPayment(@PathVariable("clientId") String clientId) {
        log.info("결제 확인 요청");
        paymentService.confirmPayment(clientId);
        return ResponseEntity.ok().build();
    }

}
