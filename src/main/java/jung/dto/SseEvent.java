package jung.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SseEvent {
    private String status;   // "connected" | "complete" | "error"
    private Object data;
}