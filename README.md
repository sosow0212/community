# Community

<hr>

### 진행 사항

- [X] 기본적인 API 설계 완료 (Done 2022.08)
- [X] 레거시 프로젝트 클린코드 적용과 리팩토링 작업 시작 (Done 2022.11.28)
- [X] Docker, Docker-compose를 이용한 환경 세팅 (Done 22.12.26)
- [X] 1차 클린코드 리팩토링 작업 완료 & 도메인 메서드 분리 작업 (Done 22.12.26)
- [X] 남은 도메인 단위 테스트 작성 (Done 22.12.26)
- [X] 확장성을 위해 Domain 기본키 Long 타입으로 리팩토링 작업 (Done 22.12.31)
- [X] User 도메인의 네이밍을 Member로 모두 변경 (Done 23.01.01)
- [X] 페이징 처리 리팩토링 작업 (Done 23.01.03)
- [ ] Filter 이용한 유저 인증 리팩토링 작업
- [ ] Redis를 이용한 공지사항, 포인트 랭킹 API 추가
- [ ] 기존에 해결한 N+1 문제 다른 방식으로 해결하기
- [ ] Jenkins 적용
- [ ] AWS HTTPS 배포

### 프로젝트 설명

- 커뮤니티 API 서버 제작 개인 프로젝트입니다.
- 성능 튜닝을 위해 JPA 설계와 테이블 설계에 집중했습니다.
- 여러가지 기술 적용으로 역량 상승을 위해 제작한 프로젝트입니다.
- 클린코드 적용과 테스트코드 작성으로 서비스의 빠른 리팩토링이 가능하도록 지속적으로 개선하고 있습니다.
- 모든 테스트 코드 총 84개 (2023.01.01 기준)

### Skills

- Spring Web
- JPA
- JUnit5 Test
- Spring Security
- JWT
- MySQL Driver
- Validation
- Swagger
- Redis
- Docker, Docker-compose

### Git Convention

- feat : 기능추가
- fix : 버그 수정
- refactor : 리팩토링, 기능은 그대로 두고 코드를 수정
- style : formatting, 세미콜론 추가 / 코드 변경은 없음
- chore : 라이브러리 설치, 빌드 작업 업데이트
- docs : 주석 추가 삭제, 문서 변경

### API 설명

- Auth : 회원가입 및 Spring Security + JWT 로그인 및 Redis를 이용해서 RefreshToken 관리 및 AccessToken 재활성화
- Member : 전체 및 개별 유저 조회, 즐겨찾기 한 글 목록 조회, 유저 정보 관련 CRUD
- Category : 계층형 카테고리 구현
- Board : 게시글 CRUD 및 검색 기능 추가, 이미지 업로드, 게시글 좋아요 및 즐겨찾기 기능
- Comment : 댓글 조회, 작성, 삭제
- Message : 쪽지 CRUD, 받은 쪽지와 보낸 쪽지 사이 둘다 삭제되면 테이블에서 Column 제거
- Report : 유저 및 게시글 신고 관리 기능
- Admin : 관리를 위한 백오피스 구현

<img width="1217" alt="image" src="https://member-images.githubusercontent.com/63213487/186867100-8983c4f6-98cf-414f-b9b6-df5ab0c05624.png">

### JUnit5 테스트

- JUnit5 를 이용해서 Domain, Service, Controller 레이어의 단위테스트를 진행했습니다.

<img width="323" alt="image" src="https://member-images.githubusercontent.com/63213487/186866768-4e091e48-e3ee-43c1-86db-aa5c81069227.png"> 
<img width="316" alt="image" src="https://member-images.githubusercontent.com/63213487/186866894-e44c50e7-572b-4298-ad84-3e1323ce5373.png">

