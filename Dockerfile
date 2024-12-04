FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/todo-list-app.jar /app/todo-list-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "todo-list-app.jar"]