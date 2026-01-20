# SOLID 원칙 정리 (Java / Spring 기준)

SOLID는 객체지향 설계에서 **유지보수성, 확장성, 테스트 용이성**을 높이기 위한  
5가지 핵심 설계 원칙이다.

> 한 줄 요약  
> **변경에 강한 코드를 만들기 위한 객체지향 설계 원칙**

---

## SOLID 구성

SOLID는 아래 5가지 원칙의 앞글자를 딴 약자다.

- **S**: Single Responsibility Principle (SRP) - 단일 책임 원칙
- **O**: Open / Closed Principle (OCP) - 개방-폐쇄 원칙
- **L**: Liskov Substitution Principle (LSP) - 리스코프 치환 원칙
- **I**: Interface Segregation Principle (ISP) - 인터페이스 분리 원칙
- **D**: Dependency Inversion Principle (DIP) - 의존 역전 원칙

---
## OCP – Open/Closed Principle
### 개방 폐쇄 원칙
** 확장에는 열려 있고, 변경에는 닫혀 있어야 한다. **

기존코드를 수정하지 않고서, 새 기능을 추가할 수 있어야 한다.
---
## DIP – Dependency Inversion Principle
### 의존 역전 원칙

**구현체가 아닌 추상화(인터페이스 또는 추상 클래스)에 의존하라.**

상위(고수준) 모듈(비즈니스 로직)은  
하위(저수준) 모듈(DB, 외부 API, 메시지 브로커 등)의 **구현 세부사항에 직접 의존하면 안 된다.**

고수준 - 변하면 안되는것
저수준 - 바뀔수 있는것 

---

### 왜 필요한가?

- 구현체에 직접 의존하면 변경에 취약해진다
- 테스트가 어렵다 (Mock/Stub 주입 불가)
- 확장이 아니라 수정이 반복된다

---

### ❌ 나쁜 예 (DIP 위반)

```java
class CancelService {
    private final MySqlCancelRepository repository =
            new MySqlCancelRepository();

    public void cancel() {
        repository.save();
    }
}
```


### ⭕️ 좋은 예 : 서비스 입장에서는 Mysql , Postgress 를 쓰는지 모른다!
```java
@RequiredArgsConstructor
class CancelService {
    private final CancelRepository repository;
    public void cancel() {
        repository.save();
    }
}
```