# 순수 Java에서 Spring Boot까지

> **"Spring이 어려운 게 아니라, 복잡한 걸 간단하게 만들어주는 거였구나!"**

## 학습 목표

이 프로젝트는 **순수 Java부터 Spring Boot까지** 단계별로 진행하며 다음을 학습합니다:

- ✅ **레이어드 아키텍처** 맛보기
- ✅ **의존성 주입(DI)** 원리와 필요성
- ✅ **애너테이션**이 동작하는 방식
- ✅ **Front Controller 패턴**과 Spring MVC의 관계
- ✅ **Spring Boot**가 해결하는 문제들 맛보기

## 3단계 학습

### 1단계: 순수 Java 도서 관리 시스템

**목표:** 콘솔 기반 CRUD 시스템 구현
**학습 포인트:** 레이어드 아키텍처, 수동 DI

**핵심 구조:**
```
 BookController   → BookService → BookRepository
         ↓               ↓              ↓
 콘솔 UI / 요청 컨트롤   비즈니스 로직    HashMap 저장소
```

**1단계 확인 방법:**
- 'Method Mapping 애너테이션 등록' 리비전 체크아웃

### 2단계: HTTP 서버 + Front Controller

**목표:** 웹 브라우저에서 접근 가능한 HTTP 서버  
**학습 포인트:** HTTP 프로토콜, Front Controller 패턴

**핵심 구조:**
```
HTTP 요청 → FrontController → Handler Mapping → BookController
    ↓            ↓                ↓               ↓
 Socket 처리   중앙 라우팅         URL 매핑          JSON 응답
```

**2단계 확인 방법:**
- 'BookController to BookService 연결' 리비전 체크아웃
- 브라우저에서 http://localhost:8080 접속

### 3단계: Spring Boot 전환

**목표:** 동일한 기능, 획기적인 코드 간소화  
**학습 포인트:** Spring의 자동화 마법

**핵심 변화:**
- ❌ **삭제:** HTTP 서버, JSON 파싱, 라우팅, DI 컨테이너
- ✅ **유지:** 비즈니스 로직 100% 보존 (BookService, 검증 등)
- 🆕 **추가:** 애너테이션 + 설정

**3단계 확인 방법:**
- master
- ./gradlew bootRun
- 브라우저에서 http://localhost:8080 접속

## 동일한 검색 기능, 다른 구현 방식

모든 단계에서 **완전히 동일한** 검색 기능을 제공합니다:

### 지원하는 검색
- **제목 검색:** "자바", "스프링" 등
- **작가 검색:** "최원빈", "남궁성" 등
- **카테고리 검색:** "프로그래밍", "소프트웨어공학" 등
- **한글 지원:** 완벽한 한글 검색 지원

### API 엔드포인트 (2단계, 3단계 동일)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/` | 웹 페이지 |
| `GET` | `/api/books` | 전체 도서 목록 |
| `GET` | `/api/books?search=키워드` | 제목 검색 |
| `GET` | `/api/books?author=작가명` | 작가 검색 |
| `GET` | `/api/books?category=카테고리` | 카테고리 검색 |
| `POST` | `/api/books` | 도서 등록 |
| `DELETE` | `/api/books/{id}` | 도서 삭제 |

## 단계별 핵심 비교

### 기술 스택 변화

| 구분 | 1단계 | 2단계 | 3단계 |
|------|-------|-------|-------|
| **UI** | 콘솔 | 웹 브라우저 | 웹 브라우저 |
| **서버** | 없음 | 순수 Java Socket | Spring Boot (Tomcat) |
| **라우팅** | 수동 분기 | FrontController | @RequestMapping |
| **DI** | 수동 생성자 | SimpleContainer | Spring Container |
| **JSON** | 수동 문자열 | 수동 파싱/생성 | Jackson 자동 |
| **DB** | HashMap | HashMap | H2 + JPA |

### 코드 복잡도 변화

```
1단계: ████████░░ (80% - 기본 구조)
2단계: ██████████ (100% - 최고 복잡도)  
3단계: ███░░░░░░░ (30% - Spring 자동화)
```

## 아키텍처 진화 과정

### 1단계: 기본 레이어드 아키텍처
```
[Console UI] → [Controller] → [Service] → [Repository] → [HashMap]
```

### 2단계: Front Controller 패턴
```
[Browser] → [HTTP Server] → [FrontController] → [Controller] → [Service] → [Repository]
                ↓                  ↓
            Socket 처리        Handler Mapping
```

### 3단계: Spring MVC 아키텍처
```
[Browser] → [DispatcherServlet] → [@RestController] → [Service] → [JPA Repository] → [H2 DB]
                ↓                       ↓                ↓            ↓
            자동 HTTP 처리           자동 JSON 변환        자동 DI      자동 쿼리 생성
```

## 실습 가이드

### 준비사항
- **Java 17+** 설치
- **IDE** (IntelliJ IDEA 등)
- **Gradle** (3단계용, Wrapper 포함)

### 권장 실습 순서

1. **1단계 실행** → 기본 구조 이해
2. **2단계 실행** → 웹 UI 체험, API 테스트
3. **3단계 실행** → 동일한 UI에서 Spring 마법 경험
4. **코드 비교** → 각 단계별 차이점 분석

### 테스트 시나리오

각 단계에서 다음을 테스트해보세요:

- ✅ **도서 등록:** "자바의 정석", "남궁성", "978-89-7914-519-7", "프로그래밍"
- ✅ **제목 검색:** "자바" 입력
- ✅ **작가 검색:** "남궁성" 입력
- ✅ **카테고리 검색:** "프로그래밍" 입력
- ✅ **도서 삭제:** 등록된 도서 삭제

## 핵심 학습 포인트

### 꼭 기억할 것들

1. **비즈니스 로직은 프레임워크와 독립적**
    - `BookService` 클래스는 3단계 모두 거의 동일
    - 좋은 설계는 기술 변화에 영향받지 않음

2. **Spring은 자동화 도구**
    - HTTP 처리, JSON 변환, DI, 트랜잭션 등을 자동화
    - 개발자는 비즈니스 로직에만 집중 가능

3. **애너테이션의 진정한 의미**
    - `@RestController` = 2단계의 FrontController + JSON 변환
    - `@RequestMapping` = 2단계의 Handler Mapping
    - `@Autowired` = 2단계의 SimpleContainer

4. **단계별 학습의 중요성**
    - 내부 동작을 이해하고 Spring을 쓰는 것과 모르고 쓰는 것은 차원이 다름

## 다음 단계 학습 추천

- **Spring Security** 추가하기
- **REST API 문서화** (Swagger)
- **테스트 코드** 작성 (JUnit)
- **Docker** 컨테이너화
- **AWS 배포** 실습

## 기여하기

이 교육 자료를 개선하고 싶다면:

1. 이슈 등록으로 개선사항 제안
2. 더 나은 예제나 설명 추가
3. 다른 언어/프레임워크 버전 구현

> **최종 목표:** "Spring이 어떻게 동작하는지 이해했고, 왜 필요한지 몸으로 깨달았다!"
