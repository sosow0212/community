# 커뮤니티 만들기

<hr>

### 프로젝트 설명
- RESTFul 한 서버를 만드는 것을 목표로 잡고 진행한 커뮤니티 프로젝트입니다.
- 최소한의 DB 조회를 위해 JPA와 데이터베이스 설계에 집중했습니다.


### Dependency
- Spring Web
- JPA
- JUnit5
- Spring Security
- JWT
- MySQL Driver
- Validation
- Swagger


### Git Convention
- feat : 기능추가
- fix : 버그 수정
- refactor : 리팩토링, 기능은 그대로 두고 코드를 수정
- style : formatting, 세미콜론 추가 / 코드 변경은 없음
- chore : 라이브러리 설치, 빌드 작업 업데이트
- docs : 주석 추가 삭제, 문서 변경


### API 설명

- Auth : 회원가입 및 Spring Security + JWT 로그인 및 refreshToken 으로 토큰 재활성화
- User : 전체 및 개별 유저 조회, 즐겨찾기 한 글 목록 조회, 유저 정보 관련 CRUD
- Board : 게시글 CRUD 및 검색 기능 추가, 이미지 업로드, 게시글 좋아요 및 즐겨찾기 기능
- Comment : 댓글 조회, 작성, 삭제
- Message : 쪽지 CRUD, 받은 쪽지와 보낸 쪽지 사이 둘다 삭제되면 테이블에서 Column 제거
- Report : 유저 및 게시글 신고 기능
- Admin : 관리를 위한 백오피스 구현