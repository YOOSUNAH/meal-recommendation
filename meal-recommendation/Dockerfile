# 첫 번째 스테이지: JAR 파일 빌드
FROM openjdk:17-slim AS build

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
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Google Chrome 설치
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && apt-get install -y google-chrome-stable && \
    rm -rf /var/lib/apt/lists/*

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







