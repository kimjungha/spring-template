### Error 설계 

1. `ErrorCode` (interface - class)
2.  `JsonProcessingException` 경우 `GlobalExceptionHandler` 처리
3. `ErrorResponse` 생성 
4.  비즈니스 에러의 경우 `BusinessException` 으로 처리
- `BusinessException` 의 역할은 "비즈니스 규칙 위반" 이라는 사실을 표현하는것

