package jung.api.webhook.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "webhook_log")
public class WebhookLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // todo : Enum 전환필요
    @Comment("이벤트 타입")
    @Column(nullable = false, length = 50)
    private String eventType;

    @Comment("웹훅 수신 대상")
    @Column(nullable = false, length = 100)
    private String webhookUrl;

    @Comment("웹훅 수신 Id(웹훅별 고유한 Id를 갖는다)")
    @Column(nullable = false, unique = true, length = 100)
    private String webhookId;

    @Comment("웹훅 보내는 내용 (추후 민감정보가 들어갔을땐 마스킹 필요) ")
    @Column(columnDefinition = "json")
    private String payload;

    // todo : Enum 전환필요
    @Comment("웹훅 상태 (PENDING, SUCCESS, RETRY,최대횟수까지 실패했을때 FAIL)")
    @Column(nullable = false, length = 20)
    private String status;

    @Comment("에러메시지(최대횟수까지실패했을경우저장)")
    @Column(length = 500)
    private String errorMessage;

    @Comment("재시도횟수")
    private int retryCount;

    @Comment("재시도시간")
    private LocalDateTime nextRetryAt;

    @Comment("웹훅 전송 최초 시간")
    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @Comment("업뎃 시간")
    private LocalDateTime updatedAt;

    @Comment("웹훅 성공했을때  응답받은시간")
    private LocalDateTime successAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        requestedAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void success(){
        status = "SUCCESS";
        successAt = LocalDateTime.now();
    }

    public void failure(){
        int maxRetry = 7;
        long baseDelaySeconds = 120;      // 1분

        if(this.retryCount > maxRetry){
            status="FAILURE";
            return;
        }
        status = "RETRY";
        retryCount++;
        long delayTime = (long) (baseDelaySeconds * Math.pow(2, this.retryCount - 1));
        nextRetryAt = LocalDateTime.now().plusSeconds(delayTime);
    }
}
