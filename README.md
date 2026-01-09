### BackEnd Study
Swagger : http://localhost:8080/swagger-ui/index.html

# AOP

# DB Replication config

# Redis 사용한 캐싱(Lettuce)

# Redisson 분산락 적용

# QueryDSL 추가

# actuator 추가 - prometheous 사용하여 메트릭 확인

# Spring Security - JWT token Login 

# Elastic Search

# Webhook 전송 및 재전송 프로세스
### 재전송, 웹훅로그정보 저장, Kafka 사용 , 초기 전송과 재전송의 내용은 동일해야한다. 


### 1️⃣ Webhook 초기 전송 요청 (Producer)

1. 비즈니스 로직의 마지막 단계에서 `sendWebhook()` 메서드를 호출한다.
2. `sendWebhook()` 메서드는 Webhook 전송에 필요한 Webhook DTO를 생성, `webhook_log` 테이블에 저장한다. 
3. Service ↔ Infra 계층 분리를 유지한 상태에서  
   `WebhookProducer`를 통해 Kafka Topic으로 메시지를 발행한다.
   - 비즈니스 로직과 Webhook 전송 로직을 분리한다.
   - Webhook 전송 실패가 비즈니스 트랜잭션에 영향을 주지 않도록 한다.

---

### 2️⃣ Webhook 전송 처리 (Consumer)

4. Kafka Consumer는 해당 Topic을 polling 하여 메시지를 가져온다.
5. Kafka Consumer는 HTTP 요청을 직접 수행하지 않고,  
   Webhook 서비스로 (`WebhookService`)로 처리를 위임한다.
   - Kafka Consumer 스레드는 polling 전용으로 사용한다. (계속되는 스레드 점유 방지, 메시지 꺼내면 바로 Service 전달)
6. `WebhookService`는 비동기 WebClient를 사용하여 Webhook HTTP POST 요청을 수행한다.

---

### 3️⃣ Webhook 전송 결과 저장

7. Webhook 응답 결과에 따라 Webhook 전송 이력을 DB에 저장한다.
   - 성공 시: `SUCCESS`
   - 실패 시: `RETRY`
   - 실패한 경우 재시도 횟수(`retry_count`) 증가
   - 다음 재시도 시간(`next_retry_at`) 계산 및 저장

---

### 4️⃣ Webhook 재전송 처리 (Scheduler)

8. Scheduler가 주기적으로 실행된다.
9. 아래 조건을 만족하는 Webhook 전송 대상을 조회한다.
`now >= next_retry_at
AND retry_count < max_retry_count
AND status = RETRY`
10. 재전송 대상으로 판단된 Webhook에 대해 재전송을 수행한다.
 - 현재 방식: `sendWebhook()` 메서드를 직접 호출하여 재전송
 - 확장 방식(권장): Kafka Topic에 다시 메시지를 발행하여 재전송
