### DistributedAop 코드 설명


* ``@Around("@annotation(jung.global.annotation.DistributedLock)")``
  * 커스텀 애노테이션을 생성하여 , 해당 애노테이션이 붙으면 AOP 실행하겠다. 
  
* ```` java
  // 예시 : key 네임, 락을 기다리는 시간 15초, 락 임대하는 시간 : 5초 
  @DistributedLock(key = "#key", waitTime = 15L, leaseTime = 5L)
  @Transactional
  public void couponDeductWithDistributed()
### 임대시간을 설정되면, 반드시 그 시간안에 코드 실행이 끝나야한다. 


* ``MethodSignature signature = (MethodSignature) joinPoint.getSignature();``
  * AOP 에서 사용되는 코드로, 현재 실행중인 메서드의 실행정보를 가져온다. 

### 📌 자바가 아닌 SpringEL(SpEL)을 사용하는 이유는? 
*   Spring Expression Language : Spring 프레임워크에서 동적으로 처리할때 사용하는 표현식 언어!
* **유지보수성을 향상시키기 위해서이다**
* 매개변수를 가져와서 동적으로 키를 만들고 있다. =>  java 로 하게 된다면 매개변수 정보에 따라서 코드가 달라진다. 
  * ❌ 문제점 1: 매개변수의 개수와 이름이 바뀌면 코드 수정 필요
    * `if ("orderId".equals(parameterNames[i])) {
      orderId = (Long) args[i];
      }`
    * 	만약 매개변수 이름이 orderId → id 로 변경되면? → 코드를 수정해야 함.
    *   매개변수가 추가된다면 -> 코드 수정해아함 
  * 각 작업마다 매개변수가 다르니, 여러 메서드에 공통적으로 적용도 어렵다!



### 📌 tryLock => 락을 획득하기 위한 메소드 
  * waitTime,leaseTime,unit을 request인자로 필요로함
  * 락 획득하지 못했다면 waitTime 까지 기다림 -> waitTime 까지 안되면 `return false`;

### 📌분산락을 수행하는 부분에서 트랜잭션이 따로 있어야하는 이유
    @Transactional(propagation = Propagation.REQUIRES_NEW)

* REQUIRES_NEW : 항상 새로운 트랜잭션을 시작하며, 부모 트랜잭션과 별개로 동작
* 분산락을 수행한 부분에서 트랜잭션이 즉시 반영되야한다.
  * 예) A 상품 주문이 들어와서 재고를 차감한다
    *  1. 검증 -> 2. 재고 차감 -> 3. 사용자 정산 금액 증가 -> 4. 주문 이력 생성 -> 5. 사용자 정산 이력 저장
    
        이런 과정을 수행하는 메서드에서 **2. 재고 차감 하는 부분** 트랜잭션이 따로 없다면, 다른 요청들이 불필요하게 대기하게됌
    * 락을 빠르게 획득하고 해제해야 성능 향상!
    * 데이터 이상이 생길 수 있음 : 2번에서 재고 차감 가능하다 해서 주문 들어가있는 과정에서 
      * 누가 늦게 들어와서 트랜잭션 커밋때려버리면 안되는 주문이 들어갈수도 있음
    