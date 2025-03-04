# 베이스 이미지 선택 (Java와 Gradle 포함)
FROM gradle:8.12.1-jdk21-corretto AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 관련 파일 복사
COPY build.gradle settings.gradle /app/
COPY src /app/src

# Gradle 빌드 수행
RUN gradle clean build -x test

# 실제 실행에 사용할 JDK 이미지를 선택
FROM openjdk:21-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
