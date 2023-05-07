FROM openjdk:19
EXPOSE 8080
WORKDIR /app
ADD /build/libs/chat-service-0.0.1-SNAPSHOT.jar chat-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","chat-service-0.0.1-SNAPSHOT.jar"]