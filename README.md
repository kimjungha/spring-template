# BackEnd Study

Spring Boot 기반의 백엔드 기술 스터디 프로젝트입니다.  
각 기능을 독립적으로 구현하며 다양한 기술 스택을 실습합니다.

---

## 개발 환경 구축

```bash
docker-compose up -d
```

| 서비스 | URL |
|---|---|
| Swagger UI | http://localhost:8080/api/swagger-ui/index.html |

### 인프라 구성

| 컴포넌트 | 포트 | 비고 |
|---|---|---|
| Elasticsearch | 9200 | |
| Kafka | 9092 | Zookeeper 의존 |
| MySQL | 3306 | (필요시 주석 해제) |
| Redis | 6379 | (필요시 주석 해제) |

---

## 구현 기능 목록

### AOP
트랜잭션 분리 등 횡단 관심사를 AOP로 처리합니다.

### DB Replication
Read / Write DB를 분리하는 Replication 설정을 구성합니다.

### Redis 캐싱 (Lettuce)
Lettuce 클라이언트를 사용한 Redis 캐싱을 적용합니다.

### Redisson 분산락
Redisson을 활용하여 분산 환경에서의 동시성 문제를 제어합니다.

### QueryDSL
타입 안전한 동적 쿼리 작성을 위해 QueryDSL을 도입합니다.

### Actuator + Prometheus
Spring Actuator와 Prometheus를 연동하여 애플리케이션 메트릭을 수집·모니터링합니다.

### Spring Security + JWT
JWT 토큰 기반의 로그인 및 인증/인가 처리를 구현합니다.

### Elasticsearch
전문 검색(Full-Text Search) 기능을 Elasticsearch로 구현합니다.

### Webhook 전송 및 재전송 프로세스
- Kafka를 사용한 비동기 전송 및 실패 시 재전송 처리
- 웹훅 로그 저장
- 멱등성 보장: 초기 전송과 재전송의 payload 동일

### SSE - 결제 상태 실시간 업데이트
Server-Sent Events를 사용하여 결제 상태 변경을 클라이언트에 실시간으로 푸시합니다.

### WebSocket - 실시간 채팅
WebSocket 기반의 단체 채팅방을 구현합니다.  
클라이언트가 메시지를 전송하면 연결된 모든 소켓에 브로드캐스트됩니다.

### gRPC
`.proto` 파일로 서비스를 정의하고 gRPC 서버를 구현합니다.

```bash
# proto 파일로 코드 생성
./gradlew generateProto
```