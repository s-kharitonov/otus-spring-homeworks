FROM bellsoft/liberica-openjdk-alpine:11.0.10-9
RUN mkdir /app
COPY /build/libs/*.jar /app/webflux-library.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/webflux-library.jar"]