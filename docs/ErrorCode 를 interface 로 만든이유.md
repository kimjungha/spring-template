
## ErrorCode 하나의 Enum 으로도 사용할 수 있다. 다만 이런경우에 문제점은 존재한다. 

### 1️⃣ Exception 이 Enum 에 종속된다. 
* BusinessException
* GlobalExceptionHandler
* ErrorResponse 

=> 결국 `ErrorCode` 에 종속되게 된다. 시간이 지날수록 해당 Enum 은 비대화 + 모든 에러코드를 갖게 된다. 

### 2️⃣ Interface 가 없는채로 관리한다면 
```
ErrorCode
├─ WALLET 관련
├─ PAYMENT 관련 
├─ (외부시스템) 관련
```
* 각각 클래스로 구성해서 필드가 다르다면 유지하기가 어렵다
```
ErrorCode (interface)
├─ CommonErrorCode
├─ WalletErrorCode
├─ AnotherSystemErrorCode
```

### 3️⃣ DIP 의존성 역전 원칙 
* 고수준 모듈은 저수준 모듈에 의존하면 안된다. (추상화에 의존해야한다. )
```java
public class BaseException {
    private final ErrorCode errorCode; //인터페이스로 선언하면 여러 ErrorCode 사용가능
}
```
  