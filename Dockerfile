# 첫 번째 스테이지: JAR 파일 빌드
FROM openjdk:17-slim AS build

COPY csv-data/data.csv /csv-data/data.csv

# 필수 패키지 설치 (curl, wget, gnupg 등)
RUN apt-get update && apt-get install -y \
    wget \
    curl \
    unzip \
    gnupg \
    libnss3 \
    libxss1 \
    libasound2 \
    libx11-xcb1 \
    libxcomposite1 \
    libxi6 \
    libxtst6 \
    fonts-liberation \
    libxrandr2 \
    libgbm1 \
    libgtk-3-0 \
    ca-certificates \
    apt-transport-https \
    dpkg \
    --no-install-recommends

# Google Chrome 설치
RUN wget -q -O /tmp/chrome.deb https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    apt-get update && \
    dpkg -i /tmp/chrome.deb || apt-get -f install -y && \
    rm /tmp/chrome.deb

# ChromeDriver 설치
RUN CHROME_DRIVER_VERSION=$(wget -qO- https://chromedriver.storage.googleapis.com/LATEST_RELEASE) && \
    wget -q https://chromedriver.storage.googleapis.com/$CHROME_DRIVER_VERSION/chromedriver_linux64.zip && \
    unzip chromedriver_linux64.zip && \
    mv chromedriver /usr/bin/chromedriver && \
    chmod +x /usr/bin/chromedriver && \
    rm chromedriver_linux64.zip

ARG JAR_FILE=build/libs/meal-recommendation-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/app.jar", \
      "--spring.profiles.active=live", \
      "--database.url=${DATABASE_URL}", \
      "--database.name=${DATABASE_NAME}", \
      "--database.username=${DATABASE_USERNAME}", \
      "--database.password=${DATABASE_PASSWORD}", \
      "--kakao.appKey=${KAKAO_REST_API_KEY}"]