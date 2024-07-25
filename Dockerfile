FROM openjdk:17

ARG JAR_FILE=build/libs/meal-recommendation-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/static/icons /app/static

ENTRYPOINT ["java","-jar","/app.jar", \
      "--spring.profiles.active=live", \
      "--database.url=${DATABASE_URL}", \
      "--database.name=${DATABASE_NAME}", \
      "--database.username=${DATABASE_USERNAME}", \
      "--database.password=${DATABASE_PASSWORD}", \
      "--kakao.appKey=${KAKAO_REST_API_KEY}"]







