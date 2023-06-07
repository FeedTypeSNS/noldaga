# 🌐 noldaga project
![놀다가 브로셔](https://github.com/FeedTypeSNS/noldaga/assets/110371892/8e8c4b84-7c8a-4348-9b21-663c987801c4)


# 🗣 프로젝트 소개
### noldaga( 놀다가❗) - 피드 형식의 통합형( 오픈 ➕ 폐쇄 ) SNS
###### 저희 팀은 개방&폐쇄형 두  SNS 특징을 잘 합친 개방형 SNS의 피로감을 줄인 (중 장년층) 모두가 익숙하게 사용 가능한 형태의 혼합형 SNS를 개발한다면, 모두가 아울러 사용할 수 있는 SNS가 될 것 이고,이런 장점은 기존의 SNS들에 질렸거나, 보다 다양한 세대의 사람들에게 충분히 메리트를 느끼고 더 넓은 시장에서 사용될 수 있을 것이라 생각해 "피드 형식의 혼합형 SNS 개발"을 주제로 선정하게 되었습니다.


- **배포 사이트** : ~~http://115.85.183.215:8080/~~ (현재 서버 종료)
- **GitHub** : https://github.com/FeedTypeSNS/noldaga/
- **Notion** : https://www.notion.so/SNS-noldaga-4cbbd09fafa34842ad911d8cf814ab36/

# 📆 제작 기간
2023.04. ~ 2023.05.15

![WBS1](https://github.com/FeedTypeSNS/noldaga/assets/110371892/ba033ab3-4c7a-40a5-a781-bf339de21e56)
![WBS2](https://github.com/FeedTypeSNS/noldaga/assets/110371892/ed262b53-6701-4795-a8bb-3c5f80c0e758)
![WBS3](https://github.com/FeedTypeSNS/noldaga/assets/110371892/308dfc51-e4e2-48de-8049-866f0e7c19ed)


# <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/bba0781b-5c72-4f27-bfbb-493b7ba8917b" height="38"> 팀 멤버 & 담당 기능
|이름|대표 기능|세부 구현|
|-----|---|---|
|<a href="https://github.com/ggiou">마지우</a>|<div align="center">채팅</div>|실시간 채팅(+ 메신저), 팔로우 서비스, 피드 추천 알고리즘, 이미지 업로드를 위한 ObjectStorage(S3)연동, NaverCloudPlatform 서버 배포 및 유지보수|
|<a href="https://github.com/Chaejiwan">채지완</a>|<div align="center">그룹</div>|그룹 서비스, 그룹 가입 및 탈퇴, 관심그룹 설정, 마이페이지 관심그룹 설정, 그룹 관리자|
|<a href="https://github.com/areumh01">한아름</a>|<div align="center">피드</div>|피드 서비스, 댓글 서비스, 해시태그, 좋아요, 저장하기, 검색 기능, 페이지 구성(헤더, 마이페이지, 메인페이지)|
|<a href="https://github.com/deliciousdev">홍재윤</a>|<div align="center">회원</div>|로그인, 소셜로그인, 회원가입, 비밀번호찾기, 아이디찾기, 회원정보수정, 이벤트 알림|

# 💫 UI & 기능 소개
### 1) 메인페이지 : private 노출, 헤더 및 사이드바....
######  <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/081d6ae7-277b-4afc-ad2a-845b8e5a33ec" width="150">   <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/cab8f255-877d-42f7-a16c-85d5c93adc0f" width="150">


### 2) 마이페이지 : 내가올린 게시물, 저장된 그룹 게시물 ,좋아요한 게시물, 친구목록, 즐겨찾기한 그룹, 프로필수정 
######  <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/42bdde70-71d1-4a36-9699-4cb9278a1c59" width="150">  <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/e66a5b67-001e-4b70-8ea7-ebb84ef3a1e7" width="150">  <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/392e6ba7-d196-4ae6-aad8-1c6a691d51c3" width="150">
  

### 3) 그룹페이지 : 내가 만든 그룹, 내가 가입한 그룹, 그룹 생성, 그룹탈퇴, 그룹 삭제  
###### <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/efc55305-8621-4fc3-b77b-f3516b297468" width="150">  <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/c597e4b6-26e1-428d-8b32-21acd21f577b" width="150">

### 4) 그룹상세페이지 : 그룹 게시물 목록, 그룹게시물 등록, 그룹 멤버 목록, 그룹 즐겨찾기 하기, 그룹탈퇴하기 ,그룹 멤버 강퇴, 그룹 정보 수정
###### <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/e347a11f-4f57-42bd-bc6f-89021448920d" width="150">  <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/7cfa608c-f8a2-46a6-ae86-cea8961782ba" width="150">

### 5) 검색페이지 : 게시글, 해시태그, 사람, 그룹 검색
###### <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/0e764238-bf98-4844-b278-a210e2411e8e" width="150"> <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/5f42c983-4957-421d-8030-b0198e3b34ef" width="150">

### 6) 탐색페이지 : public 노출....
###### <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/3ad6e45a-4b50-4c3c-9d18-cf4c66a1f5e7" width="150">

### 7) 피드상세페이지 : 피드 이미지 목록, 댓글, 좋아요, 피드 수정 및 삭제, 댓글 좋아요 , 조회수
###### <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/6402a023-1d89-41be-9875-2e33d40a6fea" width="150">

### 8) 채팅페이지 : 실시간 채팅 ,파일첨부, 채팅방 만들기, 채팅방 목록, 채팅방 관리 등..
###### <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/31b4fd75-3d87-4bc3-b9ca-e9782c6c7849" width="150">  <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/2dbd2740-e917-422f-bd15-e3d3f9394b2c" width="150">

### 9) 알림페이지 : 이벤트 알림, 알림 확인, 알림 삭제, 알림 관련 이동 링크
###### <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/be1fe375-a53a-4036-8778-173b9e5341f8" width="150">

### 10) 회원정보 수정 페이지
###### <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/383aebfb-00f0-4582-9c5d-7dc6a7136ab5" width="150">


### 11) 회원가입,로그인 : 일반회원가입, 로그인 ,소셜 회원가입,아이디,비밀번호 찾기 ,이메일 인증
###### <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/25ce32f4-e14c-4e7e-bfd4-e777751ec321" width="150">  <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/138e3949-ea86-438c-9fe5-690b24924580" width="150">  <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/0c2b7fc0-5911-45af-a476-04932a0628c7" width="150">  <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/341adf48-8b85-4381-8247-f6d2dfa01d48" width="150">  <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/5f388e06-af4e-4904-85a4-7a20c9c1f396" width="150">






# 📸 시연 영상
![](https://github.com/FeedTypeSNS/noldaga/assets/110371892/b48dcf92-0a5f-41d2-928f-eed419bb8a6c)

(https://youtu.be/N70LYCafH-o)

# 📑 분석 설계 문서
## 🟥 플로우 차트
![그림2](https://github.com/FeedTypeSNS/noldaga/assets/110371892/e64ff2f1-6cfd-41fd-a04e-8e60bd496a72)


## 🟧 와이어 프레임
![그림1](https://github.com/FeedTypeSNS/noldaga/assets/110371892/58fe6d1a-00cc-4c6d-9d3c-831b27642a11)


## 🟨 ERD 
###### <img src="https://github.com/FeedTypeSNS/noldaga/assets/127002082/62a443be-4773-47b9-ac2b-42f25e8a053a">

## 🟩 API 
![그림3](https://github.com/FeedTypeSNS/noldaga/assets/110371892/df78b66c-8ed7-44d9-816b-ff95d0de16aa)
#### 총 78개의 RESTful API
<자세히 보기> [https://docs.google.com/spreadsheets/d/154Tt38Vx4_lT0dUWdUqUL00T53AQoVPn/edit?usp=sharing&ouid=105172390124794587485&rtpof=true&sd=true]

# 🔗 서비스 아키텍쳐 
![image](https://github.com/FeedTypeSNS/noldaga/assets/110371892/b6963edc-65d2-4dcf-8034-56e01dbcb35f)

# 🛠 기술 Stack & Tools
**협업 Tools** : 
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white">
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white">
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white">
<img src="https://img.shields.io/badge/zoom-2455F8?style=for-the-badge&logo=zoom&logoColor=white">
<img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
<img src="https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white">
<img src="https://img.shields.io/badge/googlecloud-4285F4?style=for-the-badge&logo=googlecloud&logoColor=white">


**BackEnd** : 
<img src="https://img.shields.io/badge/Java11-007396?style=for-the-badge&logo=Java11&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
<img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white">  
<img src="https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=Ubuntu&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> 
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white"> 
<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"> 

**✅ amazon이 아닌 naver cloud platform을 통해 사용 (server&objectStorage)**


**FrontEnd** : 
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
<img src="https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white">
<img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white">
<img src="https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=Axios&logoColor=white">



