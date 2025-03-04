FROM gradle:8.12.1-jdk21-corretto AS builder

WORKDIR /app

# Gradle 빌드에 필요한 파일 복사
COPY build.gradle ../settings.gradle /app/
COPY pyler-common /app/pyler-common
COPY pyler-imagehub-server /app/pyler-imagehub-server

WORKDIR /app/pyler-imagehub-server

# 모듈별 Boot JAR 빌드
RUN gradle clean bootJar -x test

# 2. 실행 단계 (Slim JDK 이미지 사용)
FROM openjdk:21-jdk-slim

WORKDIR /app/pyler-imagehub-server

# 빌드된 JAR 파일 복사
COPY --from=builder /app/pyler-imagehub-server/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
