# Build: compila o JAR com o Maven Wrapper (JDK 25)
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw -q -DskipTests package

# Execução: só o JRE + JAR. O Render define a porta na variável PORT.
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/aluguel-carros-1.0.0.jar app.jar
EXPOSE 8080
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
