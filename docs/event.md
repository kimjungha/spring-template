
Explain 
Webhook 재전송


mybatis -> QueryDSL
상황
- 스프링 부트 3.2 부터 MyBatis 페이징 대표 플러그인 PageHelper 호환 이슈가 간헐적으로 발생
- PageHelper 가 적용된 일부 데이터 조회 API 의 경우, 500 Error 가 발생하거나 페이징이 미적용

해결
- PageHelper 와 호환이 되는 스프링 부트 버전을 내리거나, 페이징을 직접 하는 방안이 있었지만 
SQL 오류를 컴파일 시점에 검증할 수 있고, 타입안전성과 동적쿼리+페이징 안정성을 제공하는 QueryDSL 기반으로 리팩토링 진행 
- PageHelper 의존 제거 후에 QueryDSL 의 offset / limit 기반 페이징 적용

결과 
- PageHelper 가 적용된 일부 데이터 조회 API 의 경우, QueryDSL 페이징 구조로 전환
- 런타임 페이징 오류 및 500 에러 제거
- 동일한 조건 (where 절) 을 함수화하여, 중복코드제거+유지보수성+생산성 향상 

