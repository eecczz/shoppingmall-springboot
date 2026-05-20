# PaymentAPI (Shoppingmall Demo)

Spring Boot + MariaDB 기반 쇼핑몰 데모 프로젝트입니다.  
상품 조회 → 상세 보기 → 장바구니 → 결제(PortOne 테스트) 흐름을 **Thymeleaf** 화면으로 제공합니다.

---

## 🌐 배포 주소 (jcloud)
학교 클라우드 **jcloud** 환경(Ubuntu)에 배포되어 있으며, 외부에서 아래 주소로 접속 가능합니다.

- **외부 접속 URL:** `http://113.198.66.75:10134/demo/list`

> 포트(10134)가 외부에서 열려 있어야 접속됩니다. 접속이 안 되면 jcloud 방화벽/보안그룹 인바운드 설정을 확인하세요.

---

## 1) 주요 기능
- 회원가입 / 로그인 / 로그아웃 (세션 기반)
- 상품 목록 조회 (검색 + 페이징)
- 상품 상세 보기
- 장바구니 담기 / 삭제 / 수량 변경
- 구매 페이지
- PortOne(아임포트) 결제 검증(테스트 연동)

---

## 2) 기술 스택
- Backend: Spring Boot 3.3.x, Spring MVC
- DB: MariaDB
- ORM: Spring Data JPA, Querydsl
- View: Thymeleaf
- Deploy: Ubuntu(jcloud) + Executable Jar

---

## 3) 로컬 실행 방법
### 3-1. 요구사항
- Java 17
- MariaDB
- Gradle

### 3-2. DB 생성 및 권한(예시)
```sql
CREATE DATABASE IF NOT EXISTS tpsm
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER 'tpsmuser'@'localhost' IDENTIFIED BY 'YOUR_PASSWORD';
GRANT ALL PRIVILEGES ON tpsm.* TO 'tpsmuser'@'localhost';
FLUSH PRIVILEGES;
