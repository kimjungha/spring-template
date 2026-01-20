
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

  
* ❌ DIP 위반
  `BusinessException → WalletErrorCode (구현)`
* ✅ DIP 준수
```  
BusinessException → ErrorCode (interface)
WalletErrorCode → ErrorCode (implements)
```

### ErrorCode 를 단일 Enum 으로 관리하면
Exception / Handler / Response 가 특정 Enum 구현에 종속된다.
이는 도메인 확장 시 Enum 비대화와 높은 결합도를 유발한다.

### 따라서 ErrorCode 를 interface 로 추상화하고
도메인별 Enum 이 이를 구현하도록 하여
고수준 모듈이 추상화에만 의존하도록 설계한다.
이는 DIP 를 만족하며, 도메인 분리와 확장에 유리하다.


```java
public class BaseException {
    private final ErrorCode errorCode; //인터페이스로 선언하면 여러 ErrorCode 사용가능
}
```
  