# Microservicios Spring-Boot Workshop :leaves::cloud:

<br />

## Índice  📰
1. [Pre-Requisitos](#pre-requisitos-pencil)
2. [Librerias commons](#Librerias-commons-books)
3. [Configuración y despliegue del microservicio Eureka](#Configuración-y-despliegue-del-microservicio-Eureka-registered)
4. [Configuración y despliegue de microservicios](Configuración-y-despliegue-del-microservicios)
5. [Configuración y despliegue de microservicio Gateway]((#Configuración-y-despliegue-del-microservicio-Gateway)
6. [Acceder a la aplicación](#Acceder-a-la-aplicación-computer)
7. [Referencias](#Referencias-book)
8. [Autores](#Autores-black_nib)

## Pre Requisitos :pencil:
* Contar con una cuenta en <a href="https://cloud.ibm.com/"> IBM Cloud</a>.
* Tener instalada la CLI de Docker.
* Tener instalado Git.
* 
<br />

## Librerias commons :books:
Existe las librerias commons, que son proyectos creados en sprigboot para ser utilizadas por los microservicios como librerias, es decir, que contienen clases, modelos o servicios que son utilizadas por varios de los microservicios y no hace falta volverlos a crear en cada microservicio. Cuando estamos trabajando el proyecto localmente basta con que estas librerias se encuentren en el mismo proyecto que los microservicios para poder hacer uso de sus clases incluyendolos en el pom del microservicio, sin embargo en un entorno de nube necesitaremos un repositorio remoto que nos permita acceder a estos. A continuación se detallan los pasos para hacer utilizar un repositorio de github que permita a los microservicios disponer de estas librerias commons:


1. Generar el .jar de cada una de las librerias commons. En este proyecto contamos con tres librerias commons: commons-alumnos, commons-microservicios y commons-examenes. Para que se genere el .jar, acceda a la carpera del proyecto, abra la consola y ejecute el siguiente comando:

```
./mvnw clean package -DskipTests
```
Espere a que se complete satisfactoriamiente la construcción. Este procediemiento ejecutelo con cada una de las librerias commons.

2. Cree un repositorio en github llamado "mvn-repo" e incluya cada uno de lo jar generados, tenga en cuenta que cada .jar debe quedar en una carpeta especifica, siguiendo este lineamiento: 

```
<Nombre del paquete>/<Nombre del proyecto>/<Version>
```

Por ejemplo, el jar de la libreria commons-alumnos quedo incluida en la dirección com/formacion/springboot/commons/alumnos/commons-alumnos/0.0.1-SNAPSHOT/. Revise como quedaron configuradas las carpetas en el repositorio de librerias commons creado para este [proyecto](https://github.com/paulapachon/mvn-repo).
  
3. Una vez incluidos los .jar en el repositorio de github, debe incluirse en el pom de los microservicios el repositorio, con el siguiente segmento de código:
  
  ```
	<repositories>
		<repository>
			<id>project-common</id>
			<name>Project Common</name>
			<url>https://github.com/<usuario github>/mvn-repo/blob/main/</url>
		</repository>
	</repositories>
   ```
    
 Recuerde incluirlo en todos los microservicios que hagan uso de estas librerias commons.

<br />

## Configuración y despliegue del microservicio Eureka :registered:

Eureka es un servidor para el registro y localización de microservicios, balanceo de carga y tolerancia a fallos. La función de Eureka es registrar las diferentes instancias de microservicios existentes, su localización, estado, metadatos. Cada microservicio, durante su arranque, se comunicará con el servidor Eureka para notificar que está disponible, dónde está situado y sus metadatos. De esta forma Eureka mantendrá en su registro la información de todos los microservicios del ecosistema. 

Para publicar el microservicio Eureka en Openshift, se deben seguir los pasos a continuación:

1. Genere el .jar del microservicio accediento a la carpeta del microservicio, abriendo la consola y ejecutando el siguiente comando:

```
./mvnw clean package -DskipTests
```
Espere a que se complete satisfactoriamiente la construcción.

2. Una vez generado el .jar del microservicio, en springboot cree un nuevo archivo en el proyecto de Eureka llamado "Dockerfile" y complete el archivo con la siguiente información:

```
FROM openjdk:<version>
VOLUME /tmp
EXPOSE <puerto>
ADD ./target/<nombre del jar generado>.jar eureka-server.jar
ENTRYPOINT ["java","-jar","/eureka-server.jar"]

```

```Nota:``` En ```<version>``` ingrese el número de la versión de java con la que creo el proyecto. Para ```<puerto>``` ingrese el número del puerto donde desea exponer la aplicación. Y por último ingrese el ```<nombre del jar generado>``` que puede identificarlo en la carpeta target del proyecto. 
	
Para este proyecto el Dockerfile ha quedado conigurado de la siguiente forma:
```
FROM openjdk:16
VOLUME /tmp
EXPOSE 8761
ADD ./target/microservicio-eureka-0.0.1-SNAPSHOT.jar eureka-server.jar
ENTRYPOINT ["java","-jar","/eureka-server.jar"]	
```
3. Configurado el Dockerfile, procederemos a construir la imagen docker, para esto ingrese nuevamente a la carpeta del microservicio y abra la consola, a continuación ejecute el siguiente comando:
	
```
docker build -t <usuario docker>/<nombre de la imagen>:<tag> .	
```
	
```Nota:``` En ```<usuario docker>``` ingrese el nombre de usuario docker que tiene en docker desktop. Para ```<nombre de la imagen>``` ingrese el nombre del microservicio, en este caso "servicio-eureka-server". Y por último ingrese el ```<tag>``` que identifica la versión de la imagen, en este caso v1. 
	
4. Una vez construida la imagen, puede probarla localmente, para verificar que funciona correctamente. Para esto, ejecute el siguiente comando:
	
```
docker run -p 8761:8761 --name servicio-eureka-server --network springcloud <usuario docker>/servicio-eureka-server:v1
```	
```Nota:``` Si se va a probar localmente todo el proyecto, se recomienda correr el servicio con un --name especifico y además en una network especifica (Para crear esta network ejecute el comando ```docker network create springcloud```)
	
5. Ahora debemos publicar la imagen construida en DockerHub, para esto ejecute el siguiente comando:
	
```
docker push <usuario docker>/servicio-eureka-server:v1
```

6. Compruebe en DockerHub que su imagen ha sido publicada y copie la dirección de la imagen, que debe ser: ```<usuario docker>/servicio-eureka-server:v1```. Ingrese a la consola de Openshift y al proyecto donde ha desplegado la base de datos, asegurese de estar en el perfil de Developer. De click en ```+Add``` del menú lateral  e ingrese en la opción ```Container images```. A continuación complete lo siguiente:

	
* ```Image name from external registry:``` ```<usuario docker>/servicio-eureka-server:v1```
* ```Runtime:``` openjdk
* ```Application:``` Asegurese que sea el nombre del proyecto donde desplegó la base de datos.
* ```Name:``` servicio-eureka-server
* ```Resources:``` Deployment
* ```Advanced options:``` Seleccione la opción Create a route to the Application.
	
Por último de click en ```Create```.
	
7. Una vez desplegado el microservicio, de click en la ruta creada para acceder al microservicio, debe acceder a la consola convecional de Eureka.

<br />
	
## Configuración y despliegue de microservicios
<br />
	
## Configuración y despliegue de microservicio Gateway
<br />

## Referencias :book:
<br />

## Autores :black_nib:
Equipo IBM Cloud Tech Sales Colombia.
<br />
