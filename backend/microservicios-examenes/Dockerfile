FROM openjdk:11
VOLUME /tmp
EXPOSE 8020
ADD ./target/microservicios-examenes-0.0.1-SNAPSHOT.jar servicio-examenes.jar
ENTRYPOINT ["java","-jar","/servicio-examenes.jar"]