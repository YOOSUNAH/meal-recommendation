# 첫 번째 스테이지: JAR 파일 빌드
FROM openjdk:17-slim AS build

ARG JAR_FILE=build/libs/meal-recommendation-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 두 번째 스테이지: OpenJDK 및 Firefox 설치
FROM --platform=linux/amd64 ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install -y \
    openjdk-17-jdk wget gnupg2 tar locales \
    libc6-arm64-cross libc6-dev-arm64-cross \
    libgtk-3-0 libdbus-glib-1-2 libgdk-pixbuf2.0-0 \
    libx11-xcb1 libxtst6 libxrender1 libxi6 && \
    locale-gen ko_KR.UTF-8 && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ENV LANG=ko_KR.UTF-8

RUN wget -q 'https://download.mozilla.org/?product=firefox-latest-ssl&os=linux64&lang=en-US' -O firefox.tar.bz2 && \
    tar -xjf firefox.tar.bz2 && \
    mv firefox /opt/firefox && \
    ln -s /opt/firefox/firefox /usr/bin/firefox && \
    rm firefox.tar.bz2

COPY --from=build app.jar /app.jar

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/app.jar", \
      "--spring.profiles.active=live", \
      "--database.url=${DATABASE_URL}", \
      "--database.name=${DATABASE_NAME}", \
      "--database.username=${DATABASE_USERNAME}", \
      "--database.password=${DATABASE_PASSWORD}", \
      "--kakao.appKey=${KAKAO_REST_API_KEY}"]
