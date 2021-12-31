FROM openjdk:11
VOLUME /tmp
EXPOSE 8890
ADD ./target/microservicios-usuarios-0.0.1-SNAPSHOT.jar servicio-usuarios.jar
ENTRYPOINT ["java","-jar","/servicio-usuarios.jar"]