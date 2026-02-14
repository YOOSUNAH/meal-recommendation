# 오늘 점심 뭐먹지? (Meal Recommendation)

현재 위치 기반으로 주변 음식점을 랜덤 추천해주는 웹 애플리케이션

> 매일 반복되는 "점심 뭐 먹지?" 고민을 줄여주기 위해 만들었습니다.
> 회사 근처 실제 음식점 데이터를 기반으로, 원하는 카테고리의 식당을 랜덤 추천합니다.

**서비스 URL**: https://lunch.picksay.com

## 미리보기

<img width="667" alt="서비스 스크린샷" src="https://github.com/user-attachments/assets/3780b4f2-8e13-4b7e-9a32-9bdec4403668">

## 주요 기능

| 기능 | 설명 |
|------|------|
| 위치 기반 검색 | GPS로 현재 위치를 파악하고 반경 200m 이내 음식점 검색 |
| 카테고리 선택 | 한식, 중식, 일식, 양식 중 원하는 카테고리 선택 |
| 랜덤 추천 | 조건에 맞는 음식점 중 무작위 10곳 추천 |
| 재추천 | '다시 추천받기'로 새로운 10곳 재추천 |
| 검색 기록 | 최근 추천받은 내역 저장 및 재검색 |

## 기술 스택

| 구분 | 기술 |
|------|------|
| Backend | Java 17, Spring Boot 3.1.0, Spring Data JPA, QueryDSL |
| Database | MySQL 8.0 |
| Frontend | Thymeleaf, Tailwind CSS, Kakao Maps SDK |
| Infra | Docker, Nginx (Reverse Proxy), Cloudflare (HTTPS) |
| Data | 서울 열린데이터 광장 공공데이터 크롤링 (Selenium, JSoup) |
| Monitoring | Slack Webhook (에러 알림) |

## 아키텍처

```
사용자 (HTTPS)
  → Cloudflare (DNS + SSL)
  → Nginx (Reverse Proxy)
  → Spring Boot
  → MySQL
```

## 프로젝트 구조

```
├── Dockerfile                 # Multi-stage 빌드 (빌드 + 런타임 분리)
├── docker-compose.yml         # Nginx + Spring 컨테이너 구성
├── nginx/
│   ├── nginx.conf             # Nginx 메인 설정
│   └── conf.d/
│       └── spring-app.conf    # Reverse Proxy 설정
├── src/main/java/toy/ojm/
│   ├── domain/
│   │   ├── controller/        # API 및 페이지 라우팅
│   │   ├── service/           # 추천 로직, 거리 계산
│   │   ├── entity/            # Restaurant, FoodCategory, Users
│   │   ├── repository/        # JPA + QueryDSL 쿼리
│   │   └── dto/               # 요청/응답 DTO
│   ├── csv/                   # 공공데이터 크롤링 및 좌표 변환
│   └── global/
│       ├── config/            # QueryDSL, CORS, RestTemplate 설정
│       ├── error/             # 예외 처리 + Slack 에러 알림
│       └── util/              # 유틸리티
└── src/main/resources/
    ├── application.yml        # 프로필별 설정 (local, live)
    └── templates/             # Thymeleaf HTML 페이지
```

## 데이터 출처

[서울 열린데이터 광장 - 서울시 일반음식점 인허가 정보](https://data.seoul.go.kr/dataList/OA-16094/S/1/datasetView.do)
