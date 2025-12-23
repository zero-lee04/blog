# 📝 Blog API Project (Final Version)

## 1. 프로젝트 개요
- **주제**: Docker 환경 기반의 안정적인 블로그 서비스 백엔드 구축
- **핵심 기능**:
  - **게시글 및 미디어**: 게시글 CRUD, 카테고리 분류, 제목/내용 검색 기능
  - **상호작용**: 게시글 좋아요(Heart) 토글 및 댓글(Comment) 시스템
  - **인증/인가**: JWT 기반 로그인 및 사용자(USER)/관리자(ADMIN) 권한 분리
  - **자동화**: 서버 구동 시 초기 데이터(유저, 카테고리, 게시글, 좋아요) 자동 생성

## 2. 실행 방법
### 🐳 Docker 실행 (권장)
1. 프로젝트 폴더로 이동: `cd blog`
2. 환경변수 설정 파일 생성: `cp .env.example .env`
3. 서버 실행 (백그라운드): `sudo docker-compose up -d`
4. 앱 기동 로그 확인: `sudo docker logs -f blog-app`

### 💻 로컬 및 테스트 실행
- **환경**: JDK 17, MySQL 8.0, Redis
- **빌드 및 실행**: `./gradlew bootRun`
- **테스트 수행**: `./gradlew test`

## 3. 배포 및 접속 정보
> **참고**: 현재 배포 서버는 기관 네트워크(방화벽) 정책으로 인해 외부 포트(8080) 접속이 제한되어 있습니다. 내부망 테스트(`curl`)를 통해 정상 동작을 검증했습니다.

- **Base URL**: `http://113.198.66.68:8080`
- **Swagger URL**: `http://113.198.66.68:8080/swagger-ui/index.html`
- **Health URL**: `http://113.198.66.68:8080/actuator/health`
- **Postman 설정**: 제공된 `Blog.postman_collection.json`을 Import 하여 사용하세요. (`BASE_URL` 환경 변수 적용 완료)

## 4. 예시 계정 (Test Accounts)
| 역할 (Role) | 아이디 (Email) | 비밀번호 | 비고 |
| :--- | :--- | :--- | :--- |
| **ROLE_USER** | `user1@example.com` | `1234` | 일반 유저 (글쓰기, 좋아요, 댓글 가능) |
| **ROLE_ADMIN** | `admin@example.com` | `1234` | 관리자 (모든 리소스 삭제 및 관리) |

## 5. API 요약표 (Full Endpoints)
| 분류 | Method | URL | 설명 | 권한 |
| :--- | :--- | :--- | :--- | :--- |
| **인증** | POST | `/api/auth/signup` | 신규 회원가입 | 전체 허용 |
| | POST | `/api/auth/login` | 로그인 및 JWT 토큰 발급 | 전체 허용 |
| **게시글** | GET | `/api/posts` | 게시글 목록 조회 (검색/페이징 포함) | 전체 허용 |
| | GET | `/api/posts/{id}` | 게시글 상세 조회 | 전체 허용 |
| | POST | `/api/posts` | 새 게시글 작성 | `ROLE_USER` |
| | PUT | `/api/posts/{id}` | 게시글 수정 | 작성자 본인 |
| | DELETE | `/api/posts/{id}` | 게시글 삭제 | `ROLE_ADMIN` / 작성자 |
| **좋아요** | POST | `/api/posts/{id}/heart` | 좋아요 추가/취소 (Toggle) | `ROLE_USER` |
| **댓글** | POST | `/api/comments` | 댓글 작성 | `ROLE_USER` |
| | DELETE | `/api/comments/{id}` | 댓글 삭제 | `ROLE_ADMIN` / 작성자 |
| **카테고리** | GET | `/api/categories` | 전체 카테고리 목록 조회 | 전체 허용 |
| **시스템** | GET | `/actuator/health` | 서버 상태 헬스체크 | 전체 허용 |

## 6. 주요 설계 · 보안 · 성능 고려사항
- **데이터 모델링**: `Post`, `Comment`, `Heart` 간의 연관 관계를 매핑하여 복합적인 커뮤니티 기능 구현
- **보안 강화**: 
  - `.env` 파일을 통한 민감 정보 분리 및 Spring Security 기반 권한 제어
  - JWT Stateless 인증 방식을 채택하여 서버 확장성 고려
- **성능 최적화**: 
  - 페이징 처리를 통한 대량 데이터 조회 성능 확보
  - Hibernate `BatchSize` 설정을 통해 N+1 문제 해결

## 7. 시스템 상태 확인 (증빙)
- **서버 기동 증빙**: `sudo docker logs` 확인 시 Hibernate SQL(Insert) 및 "데이터 생성 완료" 문구 확인
- **응답 검증**: 서버 내 `curl -v http://localhost:8080/api/posts` 호출 시 `200 OK` 응답 확인 완료
