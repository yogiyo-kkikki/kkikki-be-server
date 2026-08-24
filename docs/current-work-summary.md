# kkikki-be-server 현재 작업 및 서버 구조 정리

- 작성일: 2026-08-05
- 기준: 현재 워크스페이스 코드 + 최근 채팅/작업 맥락

## 1) 현재까지 작업한 내용

### 1. RabbitMQ 라이프사이클 구성 정리
- `RabbitMqConfig`에서 RabbitMQ 엔드포인트(Exchange/Queue/RoutingKey) Bean을 정의했습니다.
- `ApplicationReadyEvent` 시점에 listener container를 시작하도록 구성해, 빈 초기화 시점 문제를 줄였습니다.
- `process()`에서 `SimpleMessageListenerContainer`를 생성/시작하고, 수신 메시지를 `RabbitMqDispatcher`로 전달합니다.
- `@PreDestroy`에서 listener를 안전하게 중지/해제하도록 구성했습니다.

### 2. 메시징 테스트 경로 구성
- `SampleController`의 `/sample/messages/test` API로 테스트 메시지 enqueue를 수행합니다.
- `SampleService`는 `MessageBuilder`/`MessagePacket`으로 이벤트 메시지를 생성해 `RabbitMqProducer`로 발행합니다.
- 발행 시 사용한 exchange/routing key/queue 정보와 트랜잭션 ID를 응답으로 반환합니다.

### 3. Auth 도메인 기본 API 구현
- 회원가입/로그인/토큰 재발급/로그아웃/내 정보 조회/프로필 수정 API가 구성되어 있습니다.
- `AuthService`는 `JdbcTemplate` 기반으로 `tb_user` 테이블을 사용하며, 비밀번호는 `BCryptPasswordEncoder`로 해시 처리합니다.
- Access/Refresh 토큰은 `JwtProvider`에서 생성/검증하며, refresh token은 메모리 저장소(`ConcurrentHashMap`)에 보관합니다.

### 4. 빌드 검증
- `gradlew.bat compileJava` 기준으로 현재 빌드는 성공 상태입니다.

## 2) 현재 서버 구조 (패키지 기준)

```text
src/main/java/com/example/kkikki_be_server
├─ domain
│  ├─ auth
│  │  ├─ AuthController
│  │  ├─ AuthService
│  │  ├─ AuthDto
│  │  └─ MeController
│  └─ sample
│     ├─ SampleController
│     ├─ SampleService
│     ├─ SampleDto
│     └─ SampleQueueEnqueueResponse
├─ global
│  ├─ config
│  │  ├─ RabbitMqConfig
│  │  ├─ RedisConfig
│  │  └─ WebSocketConfig
│  ├─ exception
│  │  ├─ BusinessException
│  │  ├─ ErrorCode
│  │  └─ GlobalExceptionHandler
│  ├─ response
│  │  ├─ ApiResponse
│  │  ├─ ApiResponseBuilder
│  │  ├─ ApiStatus
│  │  ├─ MessageBuilder
│  │  └─ MessagePacket
│  └─ security
│     ├─ SecurityConfig
│     ├─ JwtProvider
│     └─ JwtAuthenticationFilter
├─ infrastructure
│  ├─ messaging
│  │  ├─ rabbitmq
│  │  │  ├─ RabbitMqProducer
│  │  │  ├─ RabbitMqDispatcher
│  │  │  └─ RabbitMqConsumer
│  │  └─ redis
│  │     ├─ RedisMessagePublisher
│  │     └─ RedisMessageDispatcher
│  └─ storage
│     └─ r2
│        ├─ R2StorageClient
│        └─ R2StorageProperties
└─ monitoring
   ├─ HealthController
   └─ SwaggerConfig
```

## 3) 요청/처리 흐름 요약

### 1. 인증(Auth) 흐름
1. 클라이언트가 `/api/auth/*`로 요청
2. `AuthController`가 요청 DTO 검증 후 `AuthService` 위임
3. `AuthService`가 `JdbcTemplate`로 사용자 조회/수정 및 토큰 발급
4. `ApiResponseBuilder`를 통해 표준 응답 포맷으로 반환

### 2. RabbitMQ 테스트 발행 흐름
1. 클라이언트가 `POST /sample/messages/test` 호출
2. `SampleService`가 `MessagePacket` 생성 후 `RabbitMqProducer.send()` 호출
3. Producer가 `RabbitTemplate.convertAndSend(exchange, routingKey, message)`로 발행
4. Consumer side listener(`RabbitMqConfig` 내부 container)가 메시지 수신
5. `RabbitMqDispatcher -> RabbitMqConsumer.consume()` 순으로 전달

## 4) 설정/리소스 구조

- `src/main/resources/application.properties`
  - `springdoc.swagger-ui.path`
  - `app.rabbitmq.exchange`
  - `app.rabbitmq.queue`
  - `app.rabbitmq.routing-key`
- DB 스키마 마이그레이션
  - `db/sql/V1__init.sql`: `tb_user` 생성 및 유니크 제약
  - `db/sql/V2__add_index.sql`: 인덱스 관련 주석(현재 활성 SQL 없음)
- 메시지 매핑 파일
  - `messaging/kkikki-event-map.json`

## 5) 현재 상태 메모

- 보안 설정(`SecurityConfig`)은 현재 `anyRequest().permitAll()`로 열려 있어, 인증 강제 정책 적용 전 단계로 보입니다.
- Refresh token 저장소가 인메모리이므로 서버 재시작 시 토큰 상태가 초기화됩니다.
- TODO 문서에 적힌 구조와 실제 tracked 파일 간 일부 차이가 존재할 수 있어, 구조 기준은 본 문서 작성 시점의 실제 코드 기준으로 정리했습니다.
