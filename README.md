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
<br>
**📺 실시간 스트리밍**
 - Rtmp 서버의 실시간 방송 영상을 Nginx를 통하여 실시간 방송 스트리밍
<br>
**⏺️ 실시간 영상 되돌려 보기**
 - TS 파일을 생성하여 이전의 스트리밍 영상을 다시 볼 수 있음
 <br>
**💰 실시간 방송 채팅**
 - Web Soket을 사용하여 스트리머와 시청자가 소통 가능
 <br>
**♥️ 팔로우 기능**
 - 스트리머를 팔로우 하여 스트리머의 방송 상태를 볼 수 있음
<br>
**🔧 후원 설정 프리셋**
 - 후원 금액에 따라 출력되는 후원 알림 맨트, 후원 움짤, 메세지 색상 등을 프리셋을 통해 세부적인 추가
<br>   
**💸 실시간 후원 기능**
 - 스트리밍중 후원을 받으면 채팅 로그와 방송 화면에 도네이션이 출력됨
<br>
**💰 후원 환전 기능**
 - imp를 통해 입금 받을 계좌를 확인하고 이상 없다면 수수료를 차감하고 관리자에게 환전을 요청



## DB 다이어그램
<img src="https://github.com/user-attachments/assets/afd4478c-5bed-4b22-847e-ac9092aa1118" width="400">

## 🖥️ 화면 구성

<table>
  <tr>
    <th>메인 화면</th>
    <th>로그인 </th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/1279557b-64e0-4984-b4ce-d51a253e7268" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/3b2548e7-7e78-40b4-b2a0-9610937eb866" width="400"></td>
  </tr>
</table>

<table>
  <tr>
    <th>상품 페이지</th>
    <th>응찰 현황</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/77fb4f2e-e8ab-4f03-baa2-654a74b00787" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/34196532-d271-4f1c-8941-b301c87a090e" width="400"></td>
  </tr>
</table>

<table>
  <tr>
    <th>1대1 상담</th>
    <th>캐시 잔액조회</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/df7e9092-8b81-4e09-b2db-07498d1244f3" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/561b8f7b-d1ae-4a32-847e-e01aa09dbaeb" width="400"></td>
  </tr>
</table>





