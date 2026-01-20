### Array
배열은 **동일한 자료형의 데이터들을 연속된 메모리 공간에** 순서대로 저장하는 자료구조
- 인덱스 통해서 O(1) 로 접근 가능
- 크기가 고정되어 있지 않음
- 중간 삽입/삭제 시 데이터 이동 비용 발생 o(n)

### LinkedList
- 각 데이터가 **노드** 로 구성되어 있으며, 각 노드가 **포인터(link)** 를 통해서 **서로 연결** 된 리스트구조이다
- 메모리 상에서는 연속되어있지 않다
- 이전/다음 노드의 참조를 통해 연결
- 중간 삽입/삭제가 빠르나 o(1), 인덱스 접근은 느리다 o(n)

### HashTable
* Key와 Value를 1:1로 연관지어 저장하는 자료구조 (연관배열 구조와 동일 기능 지원 : 숫자 인덱스가 아닌 키로 값을 찾는 배열)
* Key를 이용하여 Value 도출
![img.png](image/img.pngmg.png)
* Key, Hash Function, Hash, Value, 저장소(Bucket, Slot)로 구성

### Key 고유한 값
* 저장 공간의 효율성을 위해 Hash Function에 입력하여 Hash로 변경 후 저장
* Key는 길이가 다양하기 때문에 그대로 저장하면 다양한 길이만큼 저장소 구성이 필요

### Hash Function
* Key를 Hash로 바꿔주는 역할
* 해시 충돌(서로 다른 Key가 같은 Hash가 되는 경우)이 발생할 확률을 최대한 줄이는 함수를 만드는 것이 중요

### HashTable 동작 과정
* Key -> Hash Function -> Hash Function 결과 = Hash
* Hash를 배열의 Index로 사용 (해당 Index에 Value 저장)
* HashTable 크기가 10이라면 A라는 Key의 Value를 찾을 때 hashFunction("A") % 10 연산을 통해 인덱스 값 계산하여 Value 조회

### Hash 충돌
* 서로 다른 Key가 Hash Function에서 중복 Hash로 나오는 경우
* 충돌이 많아질수록 탐색의 시간 복잡도가 O(1)에서 O(n)으로 증가

### Hash 충돌 해결 방법

* Separating Chaining (체이닝)
    * 한 Index 에 LinkedList 또는 List 로 여러개 저장
      * Linked List 사용 시 충돌이 발생하면 충돌 발생한 인덱스가 가리키고 있는 Linked List 에 노드 추가하여 Value 삽입
      ![img_1.png](image/img_1.png_1.png)
      * `index 3 → [("apple", 100) → ("banana", 200)]`
    * JDK 내부에서 사용하는 충돌 처리 방식
    * Linked List(데이터 6개 이하) 또는 Red-Black Tree(데이터 8개 이상) 사용 separatingChaining
    * 구현이 쉬우나, 충돌이 많아지면 O(n)
    * Key에 대한 Value 탐색 시에는 인덱스가 가리키고 있는 Linked List를 선형 검색하여 Value 반환 (삭제도 마찬가지)
    * Linked List 구조를 사용하기 때문에 추가 데이터 수 제약이 적은편
  
* Open addressing (개방 주소법)
  * 추가 메모리 공간을 사용하지 않고, HashTable 배열의 빈 공간을 사용하는 방법
  * Separating Chaining 방식에 비해 적은 메모리 사용
    방법은 Linear Probing, Quadratic Probing, Double Hashing ,Resizing
  
* HashTable 장점

  * 적은 리소스로 많은 데이터를 효율적으로 관리 가능
    ex.  많은 데이터를 Hash로 매핑하여 작은 크기의 시 메모리로 프로세스 관리 가능
  * 배열의 인덱스를 사용하기 때문에 빠른 검색, 삽입, 삭제 (O(1))
    * HashTable의 경우 인덱스는 데이터의 고유 위치이기 때문에 삽입 ,삭제 시 다른 데이터를 이동할 필요가 없어 삽입, 삭제도 빠른 속도 가능
  * Key와 Hash에 연관성이 없어 보안 유리
  * 데이터 캐싱에 많이 사용 (get, put 기능에 캐시 로직 추가 시 자주 hit하는 데이터 바로 검색 가능)
  
* HashTable 단점
    * 충돌 발생 가능성
    * 공간 복잡도 증가
    * 순서 무시 
    * 해시 함수에 의존


### HashTable vs HashMap
  * Key-Value 구조 및 Key에 대한 Hash로 Value 관리하는 것은 동일
  * HashTable
    * 동기, Key-Value 값으로 null 미허용 (Key가 hashcode(), equals()를 사용하기 때문)
    * 보조 Hash Function과 separating Chaining을 사용해서 비교적 충돌 덜 발생 (Key의 Hash 변형)
    
  * HashMap (Hash Table 개념을 코드로 구현한 것)
    * 비동기 (멀티 스레드 환경에서 주의)
    * Key-Value 값으로 null 허용
    
### HashTable 성능
| 연산 | 평균 시간 복잡도 | 최악 시간 복잡도 |
|---|---|---|
| 탐색 (Search) | O(1) | O(N) |
| 삽입 (Insert) | O(1) | O(N) |
| 삭제 (Delete) | O(1) | O(N) |


[“Hashtable은 메서드 전체에 synchronized가 걸린 레거시 클래스라 성능이 떨어지고,
HashMap은 동기화되지 않아 빠르지만 멀티스레드 환경에서는 ConcurrentHashMap을 사용합니다.”]()


### Stack
*   한쪽 끝에서만 자료를 넣고 뺄수 있는 LIFO 형식의 구조  
*  사용 사례 
1. 재귀 알고리즘
2. 웹 브라우저 방문기록
3. 실행 취소
4. 역순 문자열 만들기
5. 수식의 괄호 검사 
6. 후위표기법 계산 

### Queue
* 먼저 넣은 데이터가 먼저 나오는 FIFO 구조 
* 사용 사례
1. 너비 우선 탐색 
2. 캐시 구현
3. 인쇄 대기열 (우선순위가 같은 작업 예약)
4. 선입선출이 필요한 대기열 (티켓 카운터)
5. 콜센터 고객 대기시간
6. 프린트 출력 처리 
7. 프로세스 관리 

### Graph
* Graph 는 **정점(Node)** 과 **간선(Edge)** 으로 이루어진 자료구조로, 연결된 데이터 간의 관계를 표현하는데 사용된다. 
* 무방향, 방향, 가중치 그래프가 가능하다.
### Tree
* 노드로 이루어진 자료구조 
* 노드들과 노드를 연결하는 간선들로 구성되어있다. 

### Binary Heap
**완전이진트리** 기반의 자료구조로, 우선순위큐를 구현하는데 사용된다. 

Red-Black Tree
B+ Tree