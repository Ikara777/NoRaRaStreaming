# NoRaRa - Streaming


## Nginx + SpringBoot를 활용한 생방송 스트리밍 플랫폼
사이트 도메인 주소<br/>
https://norarastream.duckdns.org/<br/>
**"그림으로 채우는 나만의 공간"**
실시간 스트리밍 사이트의 영향력은 점차 커지고 있지만 사이트 구축과
서버 관리만으로 큰 비용이 발생하기 때문에 직접 사이트를 제작하여
소모되는 비용과 인력을 절약할 수 있으며 시청자와 스트리머가
활발히 소통할 수 있고 OBS 를 통한 간편한 방송 환경을 제공함.


## 👨‍💻프로젝트 인원 구성

### 개인 프로젝트
<table>
  <tr>
    <th>최동훈</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/66037df4-f7e3-433e-92a5-17b40556ae2f" width="100"></td>
  </tr>
</table>

## 🖱 사용 기술
**서버 환경**
 - Web/Was: Tomcat 9.0, Nginx, OBS
 - 실시간 통신: WebSocket
 - 운영체제: Windows 10, Linux (AWS EC2)

**개발 언어 및 프레임워크**
 - Backend: Java, Spring Boot, Spring Security, Spring Data JPA
 - Frontend: JavaScript, HTML5, CSS
 - 반응형 웹 디자인 구현 : Bootstrap
 - 템플릿 엔진: Thymeleaf

**데이터베이스**
 - MySQL 8.0
 - JPA를 활용한 객체 - 관계 매핑

**개발 도구**
 - IDE: IntelliJ IDEA
 - 버전 관리: Git, GitHub
 - 빌드 도구: Maven
 - AWS 접속: PuTTYgen
 - 파일 전송: FileZilla

**테스트 및 배포**
 - Docker 컨테이너 테스트
 - Ngrok을 통한 외부 접속 테스트
 - AWS EC2 클라우드 서버 배포
 - SSL 인증서를 활용한 HTTPS 배포
 

## 🛠 주요 기능

**🎙️ Nginx & OBS를 활용한 방송 송출**
  -  OBS 프로그램을 통한 스트리밍 데이터를 Nginx를 통하여 RTMP 서버에서 가져오게 함
  -  Nginx로 실시간 스트리밍을 비동기 처리하며 Spring Boot는 정책 로직을 처리하는 식으로 분리하여 효율성을 높임
  -  개인 스트리밍 Key를 UUID 형식으로 발급하여 방송할 수 있도록 함

**📺 실시간 스트리밍 목록 출력**
- Nginx RTMP 모듈에서 XML을 파싱하여 스트리밍 상태 정보 처리
- Spring Boot 에서 스트림 키를 통해 실시간 방송 중인 스트리머 목록 조회
- Thymeleaf를 활용한 동적 스트리밍 목록 페이지 구현
- 스트리머 프로필 이미지, 방송 설정, 썸네일 등 주기적으로 업데이트

**⏺️ 스트리밍 VOD 시스템**
  - Node.js를 활용한 TS 파일 관리 시스템 구현
  - 스트리밍 종료 시 이전 TS 파일을 삭제하여 메모리 관리
  - 저장된 TS 파일을 동한 실시간 방송 되돌려보기 기능 구현


**💰 실시간 채팅 시스템**
  - WebSocket을 활용한 실시간 양방향 통신 구현
  - 채팅 도배 방치 기능 및 메시지 크기 제한 설정
  - 채팅 로그 출력 시 사용자가 원하는 색상으로 텍스트를 바꿀 수 있음

**♥️ 팔로우 & 언팔로우 시스템**
  - JPA를 활용한 팔로우 관계 매핑 및 관리
  - 팔로우 목록 조회 시 페이지 네이션 적용
  - 팔로우한 스트리머의 방송 채널 이동 및 공지 사항 게시판 열람

**🔧 후원 설정 프리셋**
  - 프리셋 기능을 추가하여 사용자가 원하는 만큼 설정을 추가할 수 있음
  - 후원 메시지 색상, GIF 움짤, 출력 시간 등 디테일한 설정 가능

**💸 실시간 도네이션 시스템**
  - WebSocket을 활용한 실시간 도네이션 알림
  - 도네이션 내역 실시간 저장과 OBS와 연동하여 방송 화면에 알림 표시
  - 캐시 충전 방식을 통하여 캐시 잔액보다 높은 금액을 후원하지 못하게 함

**💰 후원 환전 기능**
  - Iamport를 연동하여 환전 대상자의 계좌를 조회한 후 올바를 경우 환전
  - 후원 메시지 & 후원 금액 & 후원 시간 등을 볼 수 있도록 함.
  - 토탈 환전 금액이 1만 원 이상만 환전할 수 있으며 수수료 20%가 차감됨


## ⭐프로젝트의 강점

**Nginx를 통한 효율적인 처리**
  - Nginx를 통해 많은 수의 동시 방송도 효율적으로 처리할 수 있음.

**OBS를 통한 간편한 방송**
  - OBS를 사용하므로 간편한 방송이 가능하며 RTMP서버에서 그대로 받아 전달하는 효율적인 구조가 됨.

**Spring Boot로 다양한 기능 개발**
  - MySQL, JWT 등을 적용하여 보안이 강화된 인증과 도네이션, 실시간 채팅 등 Web Soket을 통한 비동기 통신 기능을 구현.


## 사이트 흐름 구성
![image](https://github.com/user-attachments/assets/8c936779-0167-4815-91ec-239d0f3436a5)


## DB 구성
![image](https://github.com/user-attachments/assets/58b98d86-1b9a-49a0-bc17-541f018244ea)


## 🖥️ 화면 구성

<table>
  <tr>
    <th>메인 화면</th>
    <th>방송 목록 </th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/439a9f43-14ed-4a04-bd7c-cba0e1c5b8ab" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/e2470bd1-95e7-4cd3-8201-03d1f0cd65b0" width="400"></td>
  </tr>
</table>

<table>
  <tr>
    <th>로그인 화면</th>
    <th>마이페이지</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/dbc2a404-6777-481a-8cfc-a345f9060581" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/09ee5681-7180-43fc-97b9-133fab21c995" width="400"></td>
  </tr>
</table>

<table>
  <tr>
    <th>실시간 스트리밍</th>
    <th>후원 내역 확인</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/4a66b905-e9f2-4f70-b288-4810bb4285ee" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/eac4c87f-afb4-4897-a725-d9262beddc84" width="400"></td>
  </tr>
</table>

<table>
  <tr>
    <th>팔로우 목록</th>
    <th>스트리머 채널</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/670c28b1-ec81-4249-9c02-7015082b8b0d" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/d6fdfbab-1af5-4968-920f-404ba6b8d37f" width="400"></td>
  </tr>
</table>




