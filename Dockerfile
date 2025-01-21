# 첫 번째 스테이지: JAR 파일 빌드
FROM openjdk:17-slim AS build

ARG JAR_FILE=build/libs/meal-recommendation-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 두 번째 스테이지: OpenJDK 및 Firefox 설치
FROM ubuntu:20.04

# 환경 변수 설정
ENV DEBIAN_FRONTEND=noninteractive

# OpenJDK 및 locales 패키지 설치
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk wget gnupg2 tar locales && \
    locale-gen ko_KR.UTF-8 && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 환경 변수 설정
ENV LANG ko_KR.UTF-8

# Firefox 다운로드 및 설치
RUN wget -q 'https://download.mozilla.org/?product=firefox-latest-ssl&os=linux64&lang=en-US' -O firefox.tar.bz2 && \
    tar -xjf firefox.tar.bz2 && \
    mv firefox /opt/firefox && \
    ln -s /opt/firefox/firefox /usr/bin/firefox && \
    rm firefox.tar.bz2

# JAR 파일 복사
COPY --from=build app.jar /app.jar

# ENTRYPOINT 설정
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/app.jar", \
      "--spring.profiles.active=live", \
      "--database.url=${DATABASE_URL}", \
      "--database.name=${DATABASE_NAME}", \
      "--database.username=${DATABASE_USERNAME}", \
      "--database.password=${DATABASE_PASSWORD}", \
      "--kakao.appKey=${KAKAO_REST_API_KEY}"]
