# ---------- Stage 1: build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace/code
# คัดลอก pom ก่อนเพื่อให้ Docker cache dependency ไว้ (build ครั้งต่อไปเร็วขึ้น)
COPY code/pom.xml .
RUN mvn -B -q dependency:go-offline || true
COPY code/src ./src
RUN mvn -B -q -Dmaven.test.skip=true package

# ---------- Stage 2: run ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/code/target/*.jar app.jar
# โฟลเดอร์เก็บรูปที่อัปโหลด (บน Railway ให้ mount Volume ที่ /data)
RUN mkdir -p /data/uploads
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
