# 개발환경 구축
docker-compose up -d

### BackEnd Study
Swagger : http://localhost:8080/api/swagger-ui/index.html

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

# SSE - 결제 상태 실시간 업데이트

# WebSocket - 실시간 채팅 시스템
### 클라이언트 -> 서버로 메시지를 보낸경우, WebSocket 연결된 모든소켓에 메시지가 전송된다 (단체 채팅방)

# gRPC
### proto 파일 정의, gRPC 서버 구현
### proto 파일로 코드생성 :` ./gradlew generateProto`