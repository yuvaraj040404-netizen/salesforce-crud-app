FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY webapp/pom.xml .
COPY webapp/mvnw .
COPY webapp/.mvn .mvn
COPY webapp/src src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

CMD ["sh", "-c", "java -jar target/*.jar"]
