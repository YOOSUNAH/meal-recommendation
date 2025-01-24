# 첫 번째 스테이지: JAR 파일 빌드
FROM openjdk:17-slim AS build

ARG JAR_FILE=build/libs/meal-recommendation-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 두 번째 스테이지: OpenJDK 및 Firefox 설치
FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive

RUN sed -i 's/# deb/deb/g' /etc/apt/sources.list && \
    apt-get update && \
    apt-get install -y software-properties-common && \
    add-apt-repository ppa:mozillateam/ppa && \
    apt-get update && \
    apt-get install -y \
    openjdk-17-jdk wget gnupg2 tar locales \
    xvfb \
    firefox \
    dbus-x11 \
    libgtk-3-0 \
    libdbus-glib-1-2 && \
    locale-gen ko_KR.UTF-8 && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ENV LANG=ko_KR.UTF-8

COPY --from=build app.jar /app.jar

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/app.jar", \
      "--spring.profiles.active=live", \
      "--database.url=${DATABASE_URL}", \
      "--database.name=${DATABASE_NAME}", \
      "--database.username=${DATABASE_USERNAME}", \
      "--database.password=${DATABASE_PASSWORD}", \
      "--kakao.appKey=${KAKAO_REST_API_KEY}"]
