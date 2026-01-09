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

# Webhook 전송
## 재전송, 웹훅로그정보 저장, Kafka 사용 
## 초기 전송과 재전송의 내용은 동일해야한다. 
1. webhook 초기 전송을 위한 큐에 전송 (Producer)
   2. 비즈니스로직 마지막 부분에 sendWebhook 메서드를 호출 
   3. sendWebhook 메서드에 Webhook DTO 를 생성하여 WebhookProducer 호출한다 (Service - Infra 계층 분리를 한다 )
2. 큐에서 consumer 가 webhook 전송
   3. Kafka Consumer 가 해당 토픽에서 메시지를 꺼낸다. 
   4. Consumer 와 webhook 전송 메서드는 분리한다 ( Consumer 에서 HTTP Post 를 하게 되면 스레드를 계속 점유하고 있게된다, kafka consumer 스레드는 정말 polling 하는 용도)
   5. webhookSend 하는 부분도 비동기 WebClient 가 처리한다. 
   3. webhook 응답결과를 바탕으로 웹훅전송정보 DB 저장 
4. 응답실패시에는 재전송
5. 스케줄러 실행되면서 전송 대상 선별하기 
   6. now < next_retry_at  && retry_count < max_retry_count && status == RETRY 조건에 만족하는 대상을 찾는다 (DB 조회)
   7. 큐에 다시 넣어서 재전송할 수 있지만 , 일단은 sendWebhook() 메서드를 불러서 처리