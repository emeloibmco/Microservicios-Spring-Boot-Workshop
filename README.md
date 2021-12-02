# Microservicios Spring-Boot Workshop :leaves::cloud:

En esta guía se encuentra explicado el proceso paso a paso y las herramientas necesarias para el despliegue de una web app fullstack con Microservicios Spring como backend y Angular como frontend en un cluster de Red Hat OpenShift.

<br />

## Índice  📰

1. [Pre-Requisitos](#pre-requisitos-pencil)
2. [Creación de un nuevo proyecto en OpenShift](#Creación-de-un-nuevo-proyecto-en-OpenShift)
3. [Despliegue de la base de datos](#Despliegue-de-la-base-de-datos)
4. [Librerias commons](#Librerias-commons-books)
5. [Configuración y despliegue del microservicio Eureka](#Configuración-y-despliegue-del-microservicio-Eureka-registered)
6. [Configuración y despliegue de microservicios](#Configuración-y-despliegue-de-microservicios-paperclips)
7. [Configuración y despliegue del microservicio Gateway](#Configuración-y-despliegue-del-microservicio-Gateway-door)
8. [Acceder a la aplicación](#Acceder-a-la-aplicación-computer)
9. [Referencias](#Referencias-book)
10. [Autores](#Autores-black_nib)

## Pre Requisitos :pencil:
* Contar con una cuenta en <a href="https://cloud.ibm.com/"> IBM Cloud</a>.
* Tener instalada la CLI de Docker.
* Tener instalado Git.
* 
<br />

## Creación de un nuevo proyecto en OpenShift
Antes de iniciar con el despliegue de la aplicación es necesario crear un nuevo proyecto en un cluster de OpenShift, para esto tenga en cuenta los siguientes pasos:

1. Desde el menú de navegación o menú de hamburguesa seleccione ```Resource List```. Esto lo llevara a la lista de recursos, aquí seleccione el cluster en el cual desea crear un nuevo proyecto.
2. Una vez se encuentre en la ventana principal del cluster de click sobre ```OpenShift web console```, esto abrirá una nueva ventana emergente.
3. Cuando se encuentre en la consola de OpenShift seleccione el rol de ```Administrator``` y de click sobre la pestaña ```Projects```.
4. Aquí de click sobre el botón ```Create Project``` e ingrese la información que se le pide, luego de click en ```create```.

<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/OSproject.gif"></p>
																		 
<br />



## Despliegue de la base de datos
Para desplegar una base de datos MySQL sobre el proyecto que acabo de crear tenga en cuenta los siguientes pasos:

1.  Ingrese al proyecto con el rol de ```Developer```
2.  De click sobre el botón ```+Add``` y luego elija la opcion de ```Database``` en la seccion de ```Developer Catalog```.
3.  Aqui busque MySQL con la opción de filtrar por palabra clave.
4.  De click sobre ```Instantiate Template```y complete la siguiente información:
	*```Namespace```: Seleccione el proyecto que acabo de crear.
	* ```Memory Limit```: Ingrese la cantidad máxima de memoria que puede usar el contenedor.
	* ```Database Service Name```: El nombre del servicio OpenShift expuesto para la base de datos.
	* ```MySQL Connection Username```: Nombre de usuario del usuario de MySQL que se utilizará para acceder a la base de datos.
	* ```MySQL Connection Password```:  Contraseña para el usuario de conexión. 
	* ```MySQL root user Password```: Contraseña para el usuario root.
	* ```MySQL Database Name```: Nombre de la base de datos MySQL a la que se accede.
5. Luego de esto de click en ```Create```.

<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/DB.gif"></p>
																		 
<br />

## Librerias commons :books:
Existe las librerias commons, que son proyectos creados en sprigboot para ser utilizadas por los microservicios como librerias, es decir, que contienen clases, modelos o servicios que son utilizadas por varios de los microservicios y no hace falta volverlos a crear en cada microservicio. Cuando estamos trabajando el proyecto localmente basta con que estas librerias se encuentren en el mismo proyecto que los microservicios para poder hacer uso de sus clases incluyendolos en el pom del microservicio, sin embargo en un entorno de nube necesitaremos un repositorio remoto que nos permita acceder a estos. A continuación se detallan los pasos para hacer utilizar un repositorio de github que permita a los microservicios disponer de estas librerias commons:


1. Generar el .jar de cada una de las librerias commons. En este proyecto contamos con tres librerias commons: commons-alumnos, commons-microservicios y commons-examenes. Para que se genere el .jar, acceda a la carpera del proyecto, abra la consola y ejecute el siguiente comando:

```powershell
./mvnw clean package -DskipTests
```
Espere a que se complete satisfactoriamiente la construcción. Este procediemiento ejecutelo con cada una de las librerias commons.

<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/commons.gif"></p>

2. Cree un repositorio en github llamado "mvn-repo" e incluya cada uno de lo jar generados (se guardan en la carpeta target de cada proyecto), tenga en cuenta que cada .jar debe quedar en una carpeta especifica, siguiendo este lineamiento: 

```powershell
<Nombre del paquete>/<Nombre del proyecto>/<Version>
```

Por ejemplo, el jar de la libreria commons-alumnos quedo incluida en la dirección com/formacion/springboot/commons/alumnos/commons-alumnos/0.0.1-SNAPSHOT/. Revise como quedaron configuradas las carpetas en el repositorio de librerias commons creado para este [proyecto](https://github.com/paulapachon/mvn-repo).
  
3. Una vez incluidos los .jar en el repositorio de github, debe incluirse en el pom de los microservicios el repositorio, con el siguiente segmento de código:
  
  ```powershell
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

```powershell
./mvnw clean package -DskipTests
```
Espere a que se complete satisfactoriamiente la construcción.

2. Una vez generado el .jar del microservicio, en springboot cree un nuevo archivo en el proyecto de Eureka llamado "Dockerfile" y complete el archivo con la siguiente información:

```powershell
FROM openjdk:<version>
VOLUME /tmp
EXPOSE <puerto>
ADD ./target/<nombre del jar generado>.jar eureka-server.jar
ENTRYPOINT ["java","-jar","/eureka-server.jar"]

```

> NOTA: En ```<version>``` ingrese el número de la versión de java con la que creo el proyecto. Para ```<puerto>``` ingrese el número del puerto donde desea exponer la aplicación. Y por último ingrese el ```<nombre del jar generado>``` que puede identificarlo en la carpeta target del proyecto. 
	
Para este proyecto el Dockerfile ha quedado conigurado de la siguiente forma:
```powershell
FROM openjdk:16
VOLUME /tmp
EXPOSE 8761
ADD ./target/microservicio-eureka-0.0.1-SNAPSHOT.jar eureka-server.jar
ENTRYPOINT ["java","-jar","/eureka-server.jar"]	
```
3. Configurado el Dockerfile, procederemos a construir la imagen docker, para esto ingrese nuevamente a la carpeta del microservicio y abra la consola, a continuación ejecute el siguiente comando:
	
```powershell
docker build -t <usuario docker>/<nombre de la imagen>:<tag> .	
```

> NOTA: En ```<usuario docker>``` ingrese el nombre de usuario docker que tiene en docker desktop. Para ```<nombre de la imagen>``` ingrese el nombre del microservicio, en este caso "servicio-eureka-server". Y por último ingrese el ```<tag>``` que identifica la versión de la imagen, en este caso v1. 

<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/build.gif"></p>

4. Una vez construida la imagen, puede probarla localmente, para verificar que funciona correctamente. Para esto, ejecute el siguiente comando:
	
```powershell
docker run -p 8761:8761 --name servicio-eureka-server --network springcloud <usuario docker>/servicio-eureka-server:v1
```	
> NOTA: Si se va a probar localmente todo el proyecto, se recomienda correr el servicio con un --name especifico y además en una network especifica (Para crear esta network ejecute el comando ```docker network create springcloud```)

<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/run.gif"></p>
	
5. Ahora debemos publicar la imagen construida en DockerHub, para esto ejecute el siguiente comando:
	
```
docker push <usuario docker>/servicio-eureka-server:v1
```
<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/push.gif"></p>

6. Compruebe en DockerHub que su imagen ha sido publicada y copie la dirección de la imagen, que debe ser: ```<usuario docker>/servicio-eureka-server:v1```. Ingrese a la consola de Openshift y al proyecto donde ha desplegado la base de datos, asegúrese de estar en el perfil de Developer. De click en ```+Add``` del menú lateral  e ingrese en la opción ```Container images```. A continuación complete lo siguiente:

	
* ```Image name from external registry:``` ```<usuario docker>/servicio-eureka-server:v1```
* ```Runtime:``` openjdk
* ```Application:``` Asegúrese que sea el nombre del proyecto donde desplegó la base de datos.
* ```Name:``` servicio-eureka-server
* ```Resources:``` Deployment
* ```Advanced options:``` Seleccione la opción Create a route to the Application.
	
Por último de click en ```Create```.
	
<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/openshift.gif"></p>
	
7. Una vez desplegado el microservicio, de click en la ruta creada para acceder al microservicio, debe acceder a la consola convecional de Eureka.

<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/eureka.gif"></p>
<br />
	
## Configuración y despliegue de microservicios :paperclips:
Una vez ha verificado el funcionamiento de Eureka, el paso siguiente consiste en configurar y desplegar los microservicios usuarios, cursos, examenes y respuestas. Los pasos que se indican a continuación los debe repetir para cada microservicio establecido, modificando las características indicadas. Realice lo siguiente:

1. En el ```application.properties``` de cada microservicio asegúrese de completar las siguientes modificaciones, teniendo en cuenta el servicio de eureka y la conexión con la base de datos:

   ```powershell
   spring.application.name=microservicio-<nombre>
   server.port=<puerto>
   eureka.instance.instance-id=${spring.application.name}:${random.value}
   eureka.client.service-url.defaultZone=http://servicio-eureka-server:8761/eureka
   spring.datasource.url=jdbc:mysql://mysqldb:3306/db_microservices_app
   spring.datasource.username=admin
   spring.datasource.password=teamcloud2021
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
   spring.jpa.hibernate.ddl-auto=create
   logging.level.org.hibernate.SQL=debug
   ```

   Para cada microservicio tenga en cuenta:
   * ```Microservicio usuarios```: 
		* spring.application.name=microservicio-usuarios
		* server.port=8890
   * ```Microservicio cursos```: 
		* spring.application.name=microservicio-cursos 
		* server.port=8060
   * ```Microservicio examenes```: 
		* spring.application.name=microservicio-examenes 
		* server.port=8020
   * ```Microservicio respuestas```: 
		* spring.application.name=microservicio-respuestas 
		* server.port=8443
	
2. Genere el .jar de cada microservicio accediendo a la carpeta correspondiente de cada uno (donde se ubica el archivo mvnw), luego acceda a la consola y ejecute el comando:
   ```powershell
   ./mvnw clean package -DskipTests
   ```
	
3. Luego de generar el .jar de cada microservicio, cree el archivo ```Dockerfile``` con la misma estructura indicada para el servicio Eureka. 

   ```powershell
   FROM openjdk:<version>
   VOLUME /tmp
   EXPOSE <puerto>
   ADD ./target/<nombre del jar generado>.jar <servicio>.jar
   ENTRYPOINT ["java","-jar","/eureka-<servicio>.jar"]   
   ```

   Para cada microservicio de este proyecto, el Dockerfile queda de la siguiente manera:
   * Microservicio usuarios: 
	```powershell
	FROM openjdk:16
	VOLUME /tmp
	EXPOSE 8890
	ADD ./target/microservicio-usuarios-0.0.1-SNAPSHOT.jar servicio-usuarios.jar
	ENTRYPOINT ["java","-jar","/servicio-usuarios.jar"]
	```
		
   * Microservicio cursos 
	```powershell
	FROM openjdk:16
	VOLUME /tmp
	EXPOSE 8060
	ADD ./target/microservicio-cursos-0.0.1-SNAPSHOT.jar servicio-cursos.jar
	ENTRYPOINT ["java","-jar","/servicio-cursos.jar"]
	```
		
   * Microservicio examenes 
	```powershell
	FROM openjdk:16
	VOLUME /tmp
	EXPOSE 8020
	ADD ./target/microservicio-examenes-0.0.1-SNAPSHOT.jar servicio-examenes.jar
	ENTRYPOINT ["java","-jar","/servicio-examenes.jar"]
	```
		
   * Microservicio respuestas 
	```powershell
	FROM openjdk:16
	VOLUME /tmp
	EXPOSE 8443
	ADD ./target/microservicio-respuestas-0.0.1-SNAPSHOT.jar servicio-respuestas.jar
	ENTRYPOINT ["java","-jar","/servicio-respuestas.jar"]
	```

4. El paso siguiente consiste en generar la imagen Docker de cada microservicio. Para ello, dentro de la carpeta del microservicio, abra una consola y ejecute el comando:
	
	```powershell
	docker build -t <usuario docker>/<nombre de la imagen>:<tag> .	
	```
	
	> NOTA: recuerde reemplazar ```<usuario docker>``` con el nombre de usuario docker que tiene en docker desktop, ```<nombre de la imagen>``` con el nombre de cada microservicio (servicio-usuarios, servicio-cursos, servicio-examenes y servicio-respuestas) y por último ingrese el ```<tag>``` que identifica la versión de la imagen, en este caso v1.
	
5. Cuando finalice el proceso de creación de la imagen de cada microservicio, realice pruebas localmente con el comando:
	
	```powershell
	docker run -p <puerto>:<puerto> --name <nombre de la imagen> --network springcloud <usuario docker>/<nombre de la imagen>:v1
	```
	
	> NOTA: recuerde reemplazar el valor de ```<puerto>``` con el puerto indicado para cada microservicio, ```<nombre de la imagen>``` con el nombre del microservicio respectivo (servicio-usuarios, servicio-cursos, servicio-examenes y servicio-respuestas) y ```<usuario docker>``` con el nombre de usuario docker que tiene en docker desktop.
																		 
																		 
6. Verifique que cada una de las imágenes se ha publicado en DockerHub.
	
7. Despliegue la imagen de cada microservicio en la consola web de OpenShift. Asegúrese de estar en el proyecto donde tiene desplegada la base de datos y el servicio Eureka con el rol de Developer. Luego realice lo siguiente:
												
	* De click en la pestaña ```+Add``` (ubicada en el menú lateral izquierdo) e ingrese en la opción ```Container images```.
																		 
	En los siguientes campos complete:
	* ```Image name from external registry```: ```<usuario docker>/<nombre de la imagen>:v1```. Para cada microservicio coloque:
		* ```<usuario docker>/servicio-usuarios:v1```
		* ```<usuario docker>/servicio-cursos:v1```
		* ```<usuario docker>/servicio-examenes:v1```
		* ```<usuario docker>/servicio-respuestas:v1```
	
	* ```Runtime```: openjdk
	* ```Application```: Asegúrese que sea el nombre del proyecto donde desplegó la base de datos y el servicio Eureka.
	* ```Name```: nombre-servicio
	  Para cada microservicio coloque:
		* servicio-usuarios
		* servicio-cursos
		* servicio-examenes
		* servicio-respuestas
	
	* ```Resources```: Deployment.
	* ```Advanced options```: Seleccione la opción *Create a route to the Application*.
	
	Por último de click en el botón ```Create```.
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/DesplegarUsuarios.gif"></p>

8. Una vez desplegado cada uno de los microservicios, verifique que se hayan registrado en Eureka. Posteriormente, de click en la ruta creada para acceder a cada microservicio (todos deben responder con [ ] si no hay ningún dato. El único microservicio que no entrega respuesta es el servicio-respuestas). 
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/Eureka_microservicios.PNG"></p>
	
	<br />
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/ProbarUsuarios.gif"></p>
																		 
<br />
	
## Configuración y despliegue del microservicio Gateway :door:
Luego de verificar el funcionamiento de los respectivos microservicios, el paso siguiente consiste en desplegar el microservicio gateway. Spring Cloud Gateway funciona como punto de entrada a los microservicios, proporcionando características y capacidades como: enrutamiento dinámico, seguridad y monitoreo de solicitudes y llamadas que se realizan. Complete los pasos que se muestran a continuación para configurar y desplegar este microservicio:
	
1. En el ```application.properties``` del microservicio indique las rutas URL de los microservicios usuarios, cursos, examenes y respuestas, obtenidas en el paso 8 de la sección [Configuración y despliegue de microservicios](#Configuración-y-despliegue-de-microservicios-paperclips). Para esto reemplace los valores ```url_servicio_usuarios```, ```url_servicio_cursos```, ```url_servicio_examenes``` y ```url_servicio_respuestas```. Por otro lado, el puerto por defecto para este microservicio en ```8090```.

   ```powershell
   spring.application.name=microservicio-gateway
   server.port=8090

   eureka.client.service-url.defaultZone=http://servicio-eureka-server:8761/eureka

   spring.cloud.gateway.routes[0].id=microservicio-usuarios
   spring.cloud.gateway.routes[0].uri=url_servicio_usuarios
   spring.cloud.gateway.routes[0].predicates=Path=/api/alumnos/**
   spring.cloud.gateway.routes[0].filters=StripPrefix=2

   spring.cloud.gateway.routes[1].id=microservicio-cursos
   spring.cloud.gateway.routes[1].uri=url_servicio_cursos
   spring.cloud.gateway.routes[1].predicates=Path=/api/cursos/**
   spring.cloud.gateway.routes[1].filters=StripPrefix=2

   spring.cloud.gateway.routes[2].id=microservicio-examenes
   spring.cloud.gateway.routes[2].uri=url_servicio_examenes
   spring.cloud.gateway.routes[2].predicates=Path=/api/examenes/**
   spring.cloud.gateway.routes[2].filters=StripPrefix=2

   spring.cloud.gateway.routes[3].id=microservicio-respuestas
   spring.cloud.gateway.routes[3].uri=url_servicio_respuestas
   spring.cloud.gateway.routes[3].predicates=Path=/api/respuestas/**
   spring.cloud.gateway.routes[3].filters=StripPrefix=2

   spring.cloud.loadbalancer.ribbon.enabled=false
   ```

2. Genere el .jar del microservicio gateway ingresando a la carpeta correspondiente (donde se ubica el archivo mvnw), luego acceda a la consola y ejecute el comando:
   ```powershell
   ./mvnw clean package -DskipTests
   ```
	
3. Luego de generar el .jar del microservicio gateway, cree el archivo ```Dockerfile``` con la misma estructura indicada para los anteriores microservicios. 

   ```powershell
   FROM openjdk:<version>
   VOLUME /tmp
   EXPOSE <puerto>
   ADD ./target/<nombre del jar generado>.jar <servicio>.jar
   ENTRYPOINT ["java","-jar","/eureka-<servicio>.jar"]   
   ```

   Para el microservicio gateway de este proyecto, el Dockerfile queda de la siguiente manera:
   
	```powershell
	FROM openjdk:16
	VOLUME /tmp
	EXPOSE 8090
	ADD ./target/microservicio-gateway-0.0.1-SNAPSHOT.jar servicio-gateway.jar
	ENTRYPOINT ["java","-jar","/servicio-gateway.jar"]
	```

4. El paso siguiente consiste en generar la imagen Docker del microservicio. Para ello, dentro de la carpeta del microservicio, abra una consola y ejecute el comando:
	
	```powershell
	docker build -t <usuario docker>/<nombre de la imagen>:<tag> .	
	```
	
	> NOTA: recuerde reemplazar ```<usuario docker>``` con el nombre del usuario docker que tiene en docker desktop, ```<nombre de la imagen>``` con el nombre del microservicio (servicio-gateway) y por último ingrese el ```<tag>``` que identifica la versión de la imagen, en este caso v1.
	
5. Cuando finalice el proceso de creación de la imagen del microservicio gateway, realice pruebas localmente con el comando:
	
	```powershell
	docker run -p <puerto>:<puerto> --name <nombre de la imagen> --network springcloud <usuario docker>/<nombre de la imagen>:v1
	```
	
	> NOTA: recuerde reemplazar el valor de ```<puerto>``` con el puerto indicado para el microservicio, ```<nombre de la imagen>``` con el nombre del microservicio (servicio-gateway) y ```<usuario docker>``` con el nombre del usuario docker que tiene en docker desktop.
																		 
																		 
6. Verifique que la imagen se ha publicado en DockerHub.
	
7. Despliegue la imagen del microservicio gateway en la consola web de OpenShift. Asegúrese de estar en el proyecto donde tiene desplegada la base de datos,  el servicio Eureka y los demás microservicios con el rol de Developer. Luego realice lo siguiente:
												
	* De click en la pestaña ```+Add``` (ubicada en el menú lateral izquierdo) e ingrese en la opción ```Container images```.
																		 
	En los siguientes campos complete:
	* ```Image name from external registry```: ```<usuario docker>/<nombre de la imagen>:v1```. Para este caso coloque ```<usuario docker>/servicio-gateway:v1```.		
	* ```Runtime```: openjdk
	* ```Application```: Asegúrese que sea el nombre del proyecto donde desplegó la base de datos, el servicio Eureka y los demás microservicios.
	* ```Name```: servicio-gateway.	
	* ```Resources```: Deployment.
	* ```Advanced options```: Seleccione la opción *Create a route to the Application*.
	
	Por último de click en el botón ```Create```.

8. Una vez desplegado el microservicio, verifique que se haya registrado en Eureka. Luego, de click en la ruta creada para acceder al mismo. Una vez cargue la URL no observará respuesta, por lo que debe agregar al final de la ruta cada path de acuerdo al microservicio, ejemplo: ```/api/alumnos```, ```/api/cursos```, ```/api/examenes``` y ```/api/respuestas```. 
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/Eureka_gateway.PNG"></p>
	
	
<br />

## Referencias :book:
<br />

## Autores :black_nib:
Equipo IBM Cloud Tech Sales Colombia.
<br />
