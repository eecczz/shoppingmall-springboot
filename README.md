# Shopping Mall Spring Boot

## 프로젝트 개요

Spring Boot 기반 쇼핑몰 서버 사이드 프로젝트입니다. 회원가입과 로그인, 상품 조회, 장바구니, 주문, PortOne 테스트 결제 흐름을 end-to-end로 구현했습니다.

## 주요 기능

- 회원가입, 로그인, 세션 기반 인증
- 상품 목록/상세 조회
- 장바구니 상품 추가, 수량 변경, 삭제
- 주문 생성과 결제 검증
- QueryDSL 기반 검색 및 페이징
- jcloud 환경 배포 경험

## 기술 스택

- Backend: Java 17, Spring Boot, Spring MVC
- Database: MySQL, JPA, QueryDSL
- Payment: PortOne API
- Template/View: Thymeleaf
- Build: Gradle

## 로컬 실행

```bash
./gradlew build
./gradlew bootRun
```

DB 접속 정보와 PortOne API 값은 `.env.example`을 참고해 로컬 환경 변수로 설정합니다.

## 저장소 관리 기준

- DB 계정, 결제 API 키 등 민감한 값은 Git에 직접 커밋하지 않습니다.
- 로컬 `.env` 파일은 제외하고, 필요한 값은 `.env.example`로만 안내합니다.
