FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/todo-list-app-0.0.1-SNAPSHOT.jar /app/todo-list-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "todo-list-app.jar"]