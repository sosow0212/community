# 커뮤니티 만들기

<hr>

### 프로젝트 설명
- 커뮤니티 API 서버 제작 개인 프로젝트입니다.
- 성능 튜닝을 위해 JPA 설계와 테이블 설계에 집중했습니다.
- NoSQL Redis를 이용해서 JWT RefreshToken을 관리합니다.
- 자세한 내용은 <a href="https://blog.naver.com/sosow0212/222747372730">블로그</a> 링크를 따라서 보실 수 있습니다. (같은 카테고리 안에 순서대로 글을 따라가시면 됩니다.)


### Dependency
- Spring Web
- JPA
- JUnit5
- Spring Security
- JWT
- MySQL Driver
- Validation
- Swagger
- Redis
- Docker


### Git Convention
- feat : 기능추가
- fix : 버그 수정
- refactor : 리팩토링, 기능은 그대로 두고 코드를 수정
- style : formatting, 세미콜론 추가 / 코드 변경은 없음
- chore : 라이브러리 설치, 빌드 작업 업데이트
- docs : 주석 추가 삭제, 문서 변경


### API 설명

- Auth : 회원가입 및 Spring Security + JWT 로그인 및 Redis를 이용해서 RefreshToken 관리 및 AccessToken 재활성화
- User : 전체 및 개별 유저 조회, 즐겨찾기 한 글 목록 조회, 유저 정보 관련 CRUD
- Category : 계층형 카테고리 구현
- Board : 게시글 CRUD 및 검색 기능 추가, 이미지 업로드, 게시글 좋아요 및 즐겨찾기 기능
- Comment : 댓글 조회, 작성, 삭제
- Message : 쪽지 CRUD, 받은 쪽지와 보낸 쪽지 사이 둘다 삭제되면 테이블에서 Column 제거
- Report : 유저 및 게시글 신고 관리 기능
- Admin : 관리를 위한 백오피스 구현

<img width="1217" alt="image" src="https://user-images.githubusercontent.com/63213487/186867100-8983c4f6-98cf-414f-b9b6-df5ab0c05624.png">



### JUnit5 테스트 코드
- Junit5 를 이용해서 컨트롤러 및 서비스 레이어의 단위테스트를 진행했습니다.

<img width="323" alt="image" src="https://user-images.githubusercontent.com/63213487/186866768-4e091e48-e3ee-43c1-86db-aa5c81069227.png"> 
<img width="316" alt="image" src="https://user-images.githubusercontent.com/63213487/186866894-e44c50e7-572b-4298-ad84-3e1323ce5373.png">

