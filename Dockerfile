FROM openjdk:17

ENV KAKAO_REST_API_KEY=cf620856f46d903f5b7f04166e0f9ea3

ARG JAR_FILE=build/libs/meal-recommendation-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar", \
      "--kakao.appKey=${KAKAO_REST_API_KEY}"]







