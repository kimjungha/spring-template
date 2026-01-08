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
1. webhook 초기 전송을 위한 큐에 전송 (Producer)
2. 큐에서 consumer 가 webhook 전송
3. webhook 응답결과를 바탕으로 웹훅전송정보 DB 저장 
4. 응답실패시에는 재전송
5. 스케줄러 실행되면서 전송 대상 선별하기 
6. 1번으로 돌아가 재전송