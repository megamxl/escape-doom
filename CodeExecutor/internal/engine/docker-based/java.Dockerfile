FROM openjdk:17

ARG JAR_FILE=app.java

COPY ${JAR_FILE} app.java

RUN javac app.java

CMD ["timeout" ,"10","java", "app"]