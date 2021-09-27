FROM adoptopenjdk:16.0.1_9-jre-hotspot
RUN mkdir /opt/app
COPY target/quarkus-app/ /opt/app
CMD ["java", "-jar", "/opt/app/quarkus-run.jar"]