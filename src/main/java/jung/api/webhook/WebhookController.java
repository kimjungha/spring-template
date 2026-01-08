package jung.api.webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/webhook")
@Controller
@RequiredArgsConstructor
public class WebhookController {
    private final WebhookService webhookService;
    @GetMapping("/send")
    public void webhookSend(){
        webhookService.sendWebhook();
    }
}
