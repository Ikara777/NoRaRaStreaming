# NoRaRa - Streaming

## Nginx + SpringBoot를 활용한 생방송 스트리밍 플랫폼

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


## 🛠 주요 기능

**🎙️ 라이브 스트리머 리스트 출력**
 - TS 파일을 생성하여 이전의 스트리밍 영상을 다시 볼 수 있음


**📺 실시간 스트리밍**
 - Rtmp 서버의 실시간 방송 영상을 Nginx를 통하여 실시간 방송 스트리밍


**⏺️ 실시간 영상 되돌려 보기**
 - TS 파일을 생성하여 이전의 스트리밍 영상을 다시 볼 수 있음
 <br>
**💰 실시간 방송 채팅**
 - Web Soket을 사용하여 스트리머와 시청자가 소통 가능


**♥️ 팔로우 기능**
 - 스트리머를 팔로우 하여 스트리머의 방송 상태를 볼 수 있음


**🔧 후원 설정 프리셋**
 - 후원 금액에 따라 출력되는 후원 알림 맨트, 후원 움짤, 메세지 색상 등을 프리셋을 통해 세부적인 추가


**💸 실시간 후원 기능**
 - 스트리밍중 후원을 받으면 채팅 로그와 방송 화면에 도네이션이 출력됨


**💰 후원 환전 기능**
 - imp를 통해 입금 받을 계좌를 확인하고 이상 없다면 수수료를 차감하고 관리자에게 환전을 요청


## ⭐프로젝트의 강점

**Nginx를 통한 효율적인 처리**
Nginx를 통해 많은 수의 동시 방송도 효율적으로 처리할 수 있음.

**OBS를 통한 간편한 방송**
OBS를 사용하므로 간편한 방송이 가능하며 RTMP서버에서 그대로 받아 전달하는 효율적인 구조가 됨.

**Spring Boot로 다양한 기능 개발**
MySQL, JWT 등을 적용하여 보안이 강화된 인증과 도네이션, 실시간 채팅 등 Web Soket을 통한 비동기 통신 기능을 구현.


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




