## 이 칸에 구동 영상 올리면 어떨까 싶습니다.
___
# 🏥 AroundHospital
카카오 맵 API를 이용해 현재 내 위치 주변의 병원들을 마커로 표시해 주는 서비스. 마커를 클릭하면 병원의 상세 정보를 볼 수 있고, 기억하고 싶은 병원을 즐겨찾기에 저장할 수 있으며 지도를 이동시키면 해당 위치를 중심으로 새로운 마커가 갱신 됩니다.
### Skills
___
Clean Architecture, Kotiln, MVVM, Corountine Flow, Hilt, DataBinding, Navigation, Room, Retrofit, Lottie, Glide, ConstraintLayout
___  
![image](https://github.com/kyungsik-kim92/ObjectDetction/assets/93589990/7679dfe7-151b-466c-b482-23891858c963)
### 주요 기능
- MVVM + Clean Architecture
- Room 라이브러리 사용 병원 즐겨찾기 추가, 삭제 기능
- 현재 위치를 가져오는 콜백 형식의 데이터를 callbackFlow를 이용하여 Flow 형식으로 전환
- Flow 기능을 이용하여 맵의 이동이 다 끝난 후 2초 간격을 두고 주변의 마커를 갱신
- KakaoMapManager를 만들어 Map 생성&이벤트와 관련된 콜백들을 따로 분리/관리
- Kakao Map V1 → V2로 마이그레이션
- Hilt 라이브러리 의존성 주입
- Lottie Animation 라이브러리 사용 스플래시 화면 구성
- Kotlin DSL 적용

### 주요 라이브러리
![image](https://github.com/kyungsik-kim92/ObjectDetction/assets/93589990/a0e7a2a3-343b-4f1d-86d8-324c0ec58d24)


### Project Flow
![image](https://github.com/kyungsik-kim92/ObjectDetction/assets/93589990/a2ae347b-6140-4305-b9db-48a5750503d4)




