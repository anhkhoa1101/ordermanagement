# ---------- dev: chạy mvn spring-boot:run trực tiếp trong container (dùng với compose.yaml) ----------
FROM maven:3.9-eclipse-temurin-21 AS dev
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
EXPOSE 8080
CMD ["mvn", "-B", "spring-boot:run"]

# ---------- build: đóng gói jar ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package \
 && cp target/ordermanagement-*.jar target/app.jar

# ---------- runtime: JRE + jar + PostgreSQL nhúng ----------
# Mặc định (docker run -p 8080:8080 ordermanagement) tự khởi động PostgreSQL trong container.
# Nếu truyền SPRING_DATASOURCE_URL thì dùng DB bên ngoài (ví dụ docker compose).
FROM eclipse-temurin:21-jre AS runtime
RUN apt-get update \
 && apt-get install -y --no-install-recommends postgresql \
 && rm -rf /var/lib/apt/lists/*
WORKDIR /app
RUN useradd -r -u 1001 spring
COPY --from=build /workspace/target/app.jar app.jar
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
# bỏ ký tự CR (nếu file được checkout trên Windows) rồi cấp quyền chạy
RUN sed -i 's/\r$//' /usr/local/bin/docker-entrypoint.sh \
 && chmod +x /usr/local/bin/docker-entrypoint.sh
EXPOSE 8080
# Entrypoint chạy bằng root để khởi động PostgreSQL, sau đó hạ quyền xuống user "spring" cho ứng dụng Java.
ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
