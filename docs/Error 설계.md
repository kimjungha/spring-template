## Error 설계 

1. `ErrorCode` (interface - class)
2.  `JsonProcessingException` 경우 `GlobalExceptionHandler` 처리
3. `ErrorResponse` 생성 
4.  비즈니스 에러의 경우 `BusinessException` 으로 처리
- `BusinessException` 의 역할은 "비즈니스 규칙 위반" 이라는 사실을 표현하는것


## ErrorCode 하나의 Enum 으로도 사용할 수 있다. 다만 이런경우에 문제점은 존재한다.

### 1️⃣ Exception 이 Enum 에 종속된다.
* BusinessException
* GlobalExceptionHandler
* ErrorResponse

=> 결국 `ErrorCode` 에 종속되게 된다. 시간이 지날수록 해당 Enum 은 비대화 + 모든 에러코드를 갖게 된다.
* 의존성 분리가 필요하다

### 2️⃣ Interface 가 없는채로 여러개의 ErrorEnum 으로 관리한다면
```
ErrorCode
├─ WALLET 관련
├─ PAYMENT 관련 
├─ (외부시스템) 관련
```
* 각각으로 구성해서 필드가 다르다면 유지하기가 어렵다 -> 공통화가 필요하다

```
ErrorCode (interface)
├─ CommonErrorCode
├─ WalletErrorCode
├─ AnotherSystemErrorCode
```

### 3️⃣ DIP 의존성 역전 원칙
* 고수준 모듈은 저수준 모듈에 의존하면 안된다. (추상화에 의존해야한다.)
* 고수준 : 비즈니스 정책 구현
* 저수준 : 기술/세부 구현

비즈니스 정책이 기술을 A를 쓰든, B를 쓰든 의존하면 안된다. 
즉 ErrorCode 세부사항에 따라 BusinessException 가 의존하면 안된다. 

* ❌ DIP 위반
  `BusinessException → WalletErrorCode (구현)`
* ✅ DIP 준수
```  
BusinessException → ErrorCode (interface)
WalletErrorCode → ErrorCode (implements)
```

### 따라서 ErrorCode 를 단일 Enum 으로 관리하면
Exception / Handler / Response 가 특정 Enum 구현에 종속된다.
이는 에러코드가 확장 시 Enum 비대화와 높은 결합도를 유발한다.

### 따라서 ErrorCode 를 interface 로 추상화하고
따라서 ErrorCode를 interface로 추상화하여 공통 규격을 정의하고,
각 도메인(역할)에 맞는 ErrorCode를 enum으로 구현한다.
고수준 모듈은 특정 ErrorCode 구현에 의존하지 않고
ErrorCode 인터페이스에만 의존하도록 설계한다.

```java
public class BaseException {
    private final ErrorCode errorCode; //인터페이스로 선언하면 여러 ErrorCode 사용가능
}
```

  ### 하지만 모든 설계는 시스템의 규모, 확장성 등등을 고려하여 시스템에 맞게 진행하자, 오히려 오버엔지니어링이 될 수도 있다!
