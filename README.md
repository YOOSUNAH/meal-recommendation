# meal-recommendation
식사추천 웹 애플리케이션

## 프로젝트 설명
이 서비스는 점심메뉴를 고민하는 시간을 덜어주기 위해, 실제 사용자의 현재 위치를 중심으로 사용자가 선택한 카테고리(한식, 중식, 일식, 양식)의 식당을 랜덤으로 추천해줍니다.

## Why made?
회사 점심시간에 매번 무엇을 먹을지 고민하고, 사람들과 결정하던 고민을 줄여주기 위해서  실제로 회사 근처에 있는 식당들 중에서 랜덤으로 추천받고싶다는 생각이 들어서 만들게 되었다

## 기술 스택
Java 17, Spring Boot 3.1.0, MySQL8.0.35

## 기능

1. **사용자의 현재 위치 기반 추천**
    - GPS를 이용하여 사용자의 현재 위치를 파악하고, 해당 위치를 기준으로 주변의 음식점을 검색
2. **카테고리 선택**
    - 사용자는 한식, 중식, 일식, 양식 중 하나의 카테고리를 선택하여 원하는 종류의 음식을 추천
3. **음식점 데이터베이스 구축**
    - 서울 열린데이터 광장의 공공데이터를 이용하여 서울시 일반 음식점 정보를 크롤링한 후, 이를 엑셀 파일로 변환하여 MySQL 데이터베이스에 저장
4. **주변 음식점 검색**
    - 사용자의 현재 위치를 기준으로 반경 200미터 이내의 음식점들을 검색
5. **카테고리별 식당 추천**
    - 검색된 음식점들 중 사용자가 선택한 카테고리에 해당하는 식당들을 필터링하여 추천
6. **랜덤 추천**
    - 필터링된 음식점들 중 무작위로 10개의 식당을 골라 사용자에게 추천
7. **재추천 기능**
    - 사용자는 ‘다시 추천받기’ 버튼을 눌러 새로운 10개의 식당을 재추천 받기


## 예시 화면
1. 첫번째 화면 <br>
<img width="300" alt="스크린샷 2024-07-25 오후 6 07 32" src="https://github.com/user-attachments/assets/d0ec3fca-e8be-4e74-a731-fbd014b3e73e"><br>
---
2. 두번째 화면 <br>
<img width="300" alt="스크린샷 2024-07-25 오후 6 07 37" src="https://github.com/user-attachments/assets/859837fe-f9ec-4558-8450-04b6b944fb88"><br>
---
3. 세번째 화면 <br>
<img width="300" alt="스크린샷 2024-07-25 오후 6 07 47" src="https://github.com/user-attachments/assets/7c2826a9-6ebe-43d3-a718-78bf5bcab3c8"><br>
---
4. 네번째 화면 <br>
<img width="300" alt="스크린샷 2024-07-25 오후 6 08 00" src="https://github.com/user-attachments/assets/05f8bdf6-a43c-457b-8403-9921308eb14e"><br>
---
5. 다섯번째 화면 <br>
<img width="300" alt="스크린샷 2024-07-25 오후 6 09 18" src="https://github.com/user-attachments/assets/c3f391d3-d145-46a8-8acb-22ca5578986b"><br>
<img width="300" alt="스크린샷 2024-07-25 오후 6 08 07" src="https://github.com/user-attachments/assets/1225d704-9476-4c01-9a0d-56d0d17427f3"><br>
