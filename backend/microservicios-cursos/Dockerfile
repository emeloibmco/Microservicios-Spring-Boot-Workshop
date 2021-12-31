FROM openjdk:11
VOLUME /tmp
EXPOSE 8060
ADD ./target/microservicios-cursos-0.0.1-SNAPSHOT.jar servicio-cursos.jar
ENTRYPOINT ["java","-jar","/servicio-cursos.jar"]