# 📌 [OJT] 최신 밈 랭킹 사이트 프로젝트

> Spring Boot, JPA, Thymeleaf를 이용한 최신 밈 랭킹 사이트 프로젝트

## 📖 개요
이 프로젝트는 유저가 직접 참여하는 실시간 밈 트랜드 플랫폼으로 게시판 기능, 게시판 추천 기능, 추천 받은 게시물로 랭킹 기능을 구현할 계획입니다.<br>
백엔드는 Spring Boot를 사용했으며, 프론트엔드는 Thymeleaf와 Bootstrap을 사용합니다(SSR).

## 🔧 기술 스택
- **백엔드:** Spring Boot, Spring MVC, Spring Data JPA
- **데이터베이스:** H2 (개발용), 현재 미정 (운영용)
- **프론트엔드:** Thymeleaf, Bootstrap
- **빌드 도구:** Gradle
- **버전 관리:** Git & GitHub

## 📂 프로젝트 구조 (진행 중)
```
📂 OJT_board/
 ├── 📁 src/
 │   ├── 📁 main/
 │   │   ├── 📁 java/rujang/OJT_board/  # Java 소스 파일
 │   │   │   ├── 📁 controller/   # 컨트롤러 (웹 요청 처리)
 │   │   │   │   ├── CommentController.java # 댓글 컨트롤러
 │   │   │   │   ├── LoginController.java # 로그인 컨트롤러
 │   │   │   │   ├── LogoutController.java # 로그아웃 컨트롤러
 │   │   │   │   ├── MainController.java # 메인 컨트롤러
 │   │   │   │   ├── PostController.java # 게시판 컨트롤러
 │   │   │   │   ├── RecommendationController.java # 추천 컨트롤러
 │   │   │   │   └── RegisterController.java # 회원가입 컨트롤러
 │   │   │   ├── 📁 service/   # 비즈니스 로직
 │   │   │   │   ├── CommentService.java # 댓글 비즈니스 로직
 │   │   │   │   ├── PostService.java # 게시판 비즈니스 로직
 │   │   │   │   ├── RecommendationService.java # 추천 비즈니스 로직
 │   │   │   │   └── UserService.java # 유저 비즈니스 로직
 │   │   │   ├── 📁 repository/   # 데이터 접근 계층 (DAO)
 │   │   │   │   ├── CommentRepository.java # 댓글 리포지토리
 │   │   │   │   ├── PostRepository.java # 게시판 리포지토리
 │   │   │   │   ├── RecommendationRepository.java # 추천 리포지토리
 │   │   │   │   └── UserRepository.java # 유저 리포지토리
 │   │   │   ├── 📁 domain/   # 엔티티 클래스 (JPA)
 │   │   │   │   ├── Comment.java # 댓글 엔티티
 │   │   │   │   ├── Post.java # 게시판 엔티티
 │   │   │   │   ├── Recommendation.java # 추천 엔티티
 │   │   │   │   └── User.java # 유저 엔티티
 │   │   │   ├── 📁 dto/   # 데이터 전송 객체 (DTO)
 │   │   │   │   ├── PostUpdateDTO.java # 게시판 업데이트 DTO
 │   │   │   │   └── RegisterDTO.java # 회원가입 DTO
 │   │   │   ├── OjtCommunityApplication.java  # 메인 애플리케이션 클래스
 │   │   ├── 📁 resources/
 │   │   │   ├── 📁 templates/   # Thymeleaf 템플릿 파일
 │   │   │   │   ├── login.html # 로그인 화면
 │   │   │   │   ├── main.html # 메인 화면
 │   │   │   │   ├── post-detail.html # 게시글 세부 화면
 │   │   │   │   ├── post-edit.html # 게시글 수정 화면
 │   │   │   │   ├── post-form.html # 게시글 생성 화면
 │   │   │   │   ├── posts.html # 일반 게시판 화면
 │   │   │   │   └── register.html # 회원가입 화면
 │   │   │   ├── 📁 static/   # 정적 파일 (CSS, JS, 이미지)
 ├── 📁 test/   # 테스트 코드
 │   ├── 📁 java/rujang/OJT_board/  # 테스트 파일
 │   │   ├── OjtCommunityApplicationTests.java
 │   │   ├── 📁 service/
 │   │   │   ├── CommentServiceTest.java # 댓글 tdd
 │   │   │   ├── PostServiceTest.java # 게시판 tdd
 │   │   │   ├── RecommendationServiceTest.java # 추천 tdd
 │   │   │   └── UserServiceTest.java # 유저 tdd
 ├── build.gradle   # Gradle 빌드 파일
 ├── settings.gradle  # Gradle 설정 파일
 ├── README.md   # 프로젝트 문서
```

## 📌 주요 기능
- ✅ **게시글 CRUD (등록, 조회, 수정, 삭제)**
- ✅ **댓글 기능 CRUD (등록, 조회, 삭제)**
- ✅ **게시판 추천 기능 (한 유저당 한 게시판에 한 번만 추천 가능)**
- ✅ **추천순으로 랭킹 기능**
- ✅ **사용자 인증 (로그인, 회원가입)**
- ✅ **데이터베이스 사용**

## 🛠️ 개발 환경
- **Java** 21
- **Spring Boot** 3.4.2
- **Gradle**
- **H2**
- **IntelliJ IDEA**

## 🔒 보안 및 코드 품질
이 프로젝트에서는 민감 정보가 실수로 커밋되는 것을 방지하기 위해 Gitleaks를 사용하고 있어요.
#### 주요 내용:

- Gitleaks 통합:
  pre-commit 훅에 Gitleaks를 통합하여 커밋 전에 자동으로 민감 정보(예: API 키, 비밀번호 등)를 스캔합니다.

- 목적:
  안전한 코드베이스 유지를 위해 개발 과정에서 민감 정보가 포함되지 않도록 사전 검증을 실시합니다.

## 📜 라이선스
이 프로젝트는 개인 프로젝트이며, 현재 라이선스를 적용하지 않았습니다.
