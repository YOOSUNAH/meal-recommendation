FROM openjdk:17

ENV DATABASE_URL=112.186.29.246
ENV DATABASE_NAME=meal
ENV DATABASE_USERNAME=root
ENV DATABASE_PASSWORD=Dy041759!
ENV KAKAO_REST_API_KEY=cf620856f46d903f5b7f04166e0f9ea3

ARG JAR_FILE=build/libs/meal-recommendation-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar", \
      "--spring.profiles.active=live", \
      "--database.url=${DATABASE_URL}", \
      "--database.name=${DATABASE_NAME}", \
      "--database.username=${DATABASE_USERNAME}", \
      "--database.password=${DATABASE_PASSWORD}", \
      "--kakao.appKey=${KAKAO_REST_API_KEY}"]







