FROM openjdk:11
VOLUME /tmp
EXPOSE 8443
ADD ./target/microservicios-respuestas-0.0.1-SNAPSHOT.jar servicio-respuestas.jar
ENTRYPOINT ["java","-jar","/servicio-respuestas.jar"]