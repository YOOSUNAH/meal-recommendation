# ============================================
# 첫 번째 스테이지: 빌드
# ============================================
FROM eclipse-temurin:17-jdk-jammy AS builder

# 컨테이너 내부 작업 디렉토리
WORKDIR /build

# Gradle Wrapper와 설정 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# 소스 코드 복사
COPY src src

# gradlew 실행 권한 부여
RUN chmod +x gradlew

# Spring Boot JAR 빌드 (테스트 스킵)
RUN ./gradlew clean bootJar -x test --no-daemon

# ============================================
# 두 번째 스테이지: 런타임
# ============================================
FROM eclipse-temurin:17-jre-jammy

# 크롤링을 위한 필수 패키지 설치
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
    libgbm1 \
    ca-certificates \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Chromium 및 ChromeDriver 설치 (ARM64 지원)
RUN apt-get update && \
    apt-get install -y chromium-browser chromium-chromedriver && \
    rm -rf /var/lib/apt/lists/*

# Chromium 실행 파일 심볼릭 링크 생성
RUN ln -s /usr/bin/chromium-browser /usr/bin/google-chrome || true && \
    ln -s /usr/bin/chromedriver /usr/local/bin/chromedriver || true

# 애플리케이션 실행 디렉토리
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /build/build/libs/meal-recommendation-0.0.1-SNAPSHOT.jar app.jar

# 로그 디렉토리 생성
RUN mkdir -p /app/logs && \
    mkdir -p /var/meal-recommendation-logs

# 비root 사용자 생성
RUN useradd -m -u 1000 appuser && \
    chown -R appuser:appuser /app && \
    chown -R appuser:appuser /var/meal-recommendation-logs

# appuser 권한으로 실행
USER appuser

# 컨테이너 외부에 공개할 포트
EXPOSE 8081

# JVM 옵션
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -Dfile.encoding=UTF-8"

# 컨테이너 헬스체크
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/ || exit 1

# 컨테이너 실행 명령
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]







