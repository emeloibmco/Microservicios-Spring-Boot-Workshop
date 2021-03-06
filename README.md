# Microservicios Spring-Boot Workshop :leaves::cloud:

En esta gu铆a se encuentra explicado el proceso paso a paso y las herramientas necesarias para el despliegue de una web app fullstack con Microservicios Spring como backend y Angular como frontend en un cluster de Red Hat OpenShift.

<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/esquema.png"></p>
																		
<br />

## 脥ndice  馃摪

1. [Pre-Requisitos](#pre-requisitos-pencil)
2. [Creaci贸n de un nuevo proyecto en OpenShift](#Creaci贸n-de-un-nuevo-proyecto-en-OpenShift)
3. [Despliegue de la base de datos](#Despliegue-de-la-base-de-datos)
4. [Librerias commons](#Librerias-commons-books)
5. [Configuraci贸n y despliegue del microservicio Eureka](#Configuraci贸n-y-despliegue-del-microservicio-Eureka-registered)
6. [Configuraci贸n y despliegue de microservicios](#Configuraci贸n-y-despliegue-de-microservicios-paperclips)
7. [Configuraci贸n y despliegue del microservicio Gateway](#Configuraci贸n-y-despliegue-del-microservicio-Gateway-door)
8. [Acceder a la aplicaci贸n](#Acceder-a-la-aplicaci贸n-computer)
9. [Configuraci贸n del frontend en Angular](#Configuraci贸n-del-frontend-en-Angular-a)
10. [Creaci贸n de imagen frontend](#Creaci贸n-de-imagen-frontend)
11. [Despliegue de frontend en OpenShift](#Despliegue-de-frontend-en-OpenShift)
12. [NOTAS sobre temas pendientes con el frontend](#NOTAS-sobre-temas-pendientes-con-el-frontend)
13. [Referencias](#Referencias-book)
14. [Autores](#Autores-black_nib)

## Pre Requisitos :pencil:
* Contar con una cuenta en <a href="https://cloud.ibm.com/"> IBM Cloud</a>.
* Tener instalada la CLI de Docker.
* Tener instalado Git.
* Tener instalado Angular, Node JS.
<br />

## Creaci贸n de un nuevo proyecto en OpenShift
Antes de iniciar con el despliegue de la aplicaci贸n es necesario crear un nuevo proyecto en un cluster de OpenShift, para esto tenga en cuenta los siguientes pasos:

1. Desde el men煤 de navegaci贸n o men煤 de hamburguesa seleccione ```Resource List```. Esto lo llevara a la lista de recursos, aqu铆 seleccione el cluster en el cual desea crear un nuevo proyecto.
2. Una vez se encuentre en la ventana principal del cluster de click sobre ```OpenShift web console```, esto abrir谩 una nueva ventana emergente.
3. Cuando se encuentre en la consola de OpenShift seleccione el rol de ```Administrator``` y de click sobre la pesta帽a ```Projects```.
4. Aqu铆 de click sobre el bot贸n ```Create Project``` e ingrese la informaci贸n que se le pide, luego de click en ```create```.

<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/OSproject.gif"></p>
																		 
<br />



## Despliegue de la base de datos
Para desplegar una base de datos MySQL sobre el proyecto que acabo de crear tenga en cuenta los siguientes pasos:
1. Dentro de su cuenta de *IBM Cloud* acceda al ```IBM Cloud Shell``` dando click en la pesta帽a <a href="https://cloud.ibm.com/shell"> <img width="25" src="https://github.com/emeloibmco/Red-Hat-Open-Shift---Desplegar-MySQL/blob/main/Images/Shell_IBM.png"></a>, que se ubica en la parte superior derecha del portal. 
<br />

<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/IBMCloudShell.png"></p>

<br />

2. Ingrese a la consola web de OpenShift presionando el bot贸n ```OpenShift web console```. 
<br />

<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/AccederConsolaOC.png"></p>

<br />


3. Posteriormente de click sobre su correo (parte superior derecha) y luego en la opci贸n ```Copy Login Command```. Una vez cargue la nueva ventana, de click en la opci贸n ```Display Token```. Copie el comando que sale en la opci贸n ```Log in with this token``` y col贸quelo en el IBM Cloud Shell para iniciar sesi贸n y acceder a su cl煤ster de OpenShift.
<br />

<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/TokenFinal.gif"></p>

<br />

<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/TokenAccesoShell.png"></p>

<br />

4. Acceda al proyecto creado OpenShift. Para ello utilice el comando:

   ```
   oc project <nombre_proyecto>
   ```

   Ejemplo:

   ```
   oc project microservicios-spring-mysql
   ```
   <br />

5. Realice una b煤squeda en el cat谩logo sobre los recursos relacionados con MySQL que pueden ser desplegados en el cl煤ster. Para ello coloque el comando:

   ```
   oc new-app --search mysql
   ```
   <br />

   <p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/BuscarMySQL.png"></p>

   <br />

6. Despliegue el template de MySQL. Para este caso hay dos opciones que puede utilizar:
   * Sin almacenamiento persistente (```mysql```).
   * Con almacenamiento persistente (```mysql-persistent```).
   <br />
   
   Para este caso, se utiliza la plantilla con almacenamiento de volumen persistente ya que esto permite que los datos sobrevivan y no se pierdan cuando el pod se reinicie. Por otro lado, en el despliegue se deben indicar algunas variables de entorno para la configuraci贸n del servidor. Utilice el comando:
   
   ```
   oc new-app mysql-persistent --param=MYSQL_USER=user --param=MYSQL_PASSWORD=pass --param=MYSQL_DATABASE=prueba --name mysql
   ```
   
   > NOTA: Las varibles definidas permiten configurar el usuario, contrase帽a y nombre de la base de datos MySQL. Estos datos se necesitar谩n m谩s adelante cuando acceda a la base de datos. Para este caso particular los datos usados son:

   ```
   oc new-app mysql-persistent --param=MYSQL_USER=admin --param=MYSQL_PASSWORD=teamcloud2021 --param=MYSQL_DATABASE=app_microservices_db --name mysqldb
   ```
   
   <br />

   <p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/desplegar_mysql.jpeg"></p>

   <br />

7. Verifique el estado de implementaci贸n de la base de datos. Para ello coloque el comando:

   ```
   oc status
   ```

   <br />

8. Obtenga los pods de MySQL y verifique que el despliegue se ha completado con 茅xito. Utilice el comando:

   ```
   oc get pods
   ```
   <br />

   <p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/oc_pods.PNG"></p>

   <br />
   
Para acceder a la base de datos MySQL que ha desplegado en el cl煤ster de OpenShift, siga los pasos que se muestran a continuaci贸n:
<br />

9. Abra una sesi贸n de shell remota en un contenedor. Para ello, utilice el siguiente comando reemplazando ```<pod>``` con el nombre del pod de MySQL cuyo estado es ```running```:

   ```
   oc rsh <pod>
   ```
   
   Ejemplo:
   
   ```
   oc rsh mysql-1-zw2qk
   ```
   <br />

   <p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/rsh.PNG"></p>

   <br />

10. Para acceder con las credenciales definidas en el despliegue de MySQL coloque:

   ```
   mysql -u admin -p
   ```
   <br />
   
   Una vez ejecute el comando, coloque la contrase帽a establecida (```teamcloud2021```) y presione enter para continuar. Si la contrase帽a es correcta, acceder谩 con exito a la base de datos.
   
   <br />

   <p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/mysql_u_p.PNG"></p>

   <br />
 
   > NOTA: para conocer sobre las operaciones del CRUD puede consultar https://github.com/emeloibmco/Red-Hat-Open-Shift-Despliegue-MySQL.
		 
<br />

## Librerias commons :books:
Existe las librerias commons, que son proyectos creados en sprigboot para ser utilizadas por los microservicios como librerias, es decir, que contienen clases, modelos o servicios que son utilizadas por varios de los microservicios y no hace falta volverlos a crear en cada microservicio. Cuando estamos trabajando el proyecto localmente basta con que estas librerias se encuentren en el mismo proyecto que los microservicios para poder hacer uso de sus clases incluyendolos en el pom del microservicio, sin embargo en un entorno de nube necesitaremos un repositorio remoto que nos permita acceder a estos. A continuaci贸n se detallan los pasos para hacer utilizar un repositorio de github que permita a los microservicios disponer de estas librerias commons:


1. Generar el .jar de cada una de las librerias commons. En este proyecto contamos con tres librerias commons: commons-alumnos, commons-microservicios y commons-examenes. Para que se genere el .jar, acceda a la carpera del proyecto, abra la consola y ejecute el siguiente comando:

```powershell
./mvnw clean package -DskipTests
```
Espere a que se complete satisfactoriamiente la construcci贸n. Este procediemiento ejecutelo con cada una de las librerias commons.

<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/commons.gif"></p>

2. Cree un repositorio en github llamado "mvn-repo" e incluya cada uno de lo jar generados (se guardan en la carpeta target de cada proyecto), tenga en cuenta que cada .jar debe quedar en una carpeta especifica, siguiendo este lineamiento: 

```powershell
<Nombre del paquete>/<Nombre del proyecto>/<Version>
```

Por ejemplo, el jar de la libreria commons-alumnos quedo incluida en la direcci贸n com/formacion/springboot/commons/alumnos/commons-alumnos/0.0.1-SNAPSHOT/. Revise como quedaron configuradas las carpetas en el repositorio de librerias commons creado para este [proyecto](https://github.com/paulapachon/mvn-repo).
  
3. Una vez incluidos los .jar en el repositorio de github, debe incluirse en el pom de los microservicios el repositorio, con el siguiente segmento de c贸digo:
  
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

## Configuraci贸n y despliegue del microservicio Eureka :registered:

Eureka es un servidor para el registro y localizaci贸n de microservicios, balanceo de carga y tolerancia a fallos. La funci贸n de Eureka es registrar las diferentes instancias de microservicios existentes, su localizaci贸n, estado, metadatos. Cada microservicio, durante su arranque, se comunicar谩 con el servidor Eureka para notificar que est谩 disponible, d贸nde est谩 situado y sus metadatos. De esta forma Eureka mantendr谩 en su registro la informaci贸n de todos los microservicios del ecosistema. 

Para publicar el microservicio Eureka en Openshift, se deben seguir los pasos a continuaci贸n:

1. Genere el .jar del microservicio accediento a la carpeta del microservicio, abriendo la consola y ejecutando el siguiente comando:

```powershell
./mvnw clean package -DskipTests
```
Espere a que se complete satisfactoriamiente la construcci贸n.

2. Una vez generado el .jar del microservicio, en springboot cree un nuevo archivo en el proyecto de Eureka llamado "Dockerfile" y complete el archivo con la siguiente informaci贸n:

```powershell
FROM openjdk:<version>
VOLUME /tmp
EXPOSE <puerto>
ADD ./target/<nombre del jar generado>.jar eureka-server.jar
ENTRYPOINT ["java","-jar","/eureka-server.jar"]

```

> NOTA: En ```<version>``` ingrese el n煤mero de la versi贸n de java con la que creo el proyecto. Para ```<puerto>``` ingrese el n煤mero del puerto donde desea exponer la aplicaci贸n. Y por 煤ltimo ingrese el ```<nombre del jar generado>``` que puede identificarlo en la carpeta target del proyecto. 
	
Para este proyecto el Dockerfile ha quedado conigurado de la siguiente forma:
```powershell
FROM openjdk:16
VOLUME /tmp
EXPOSE 8761
ADD ./target/microservicio-eureka-0.0.1-SNAPSHOT.jar eureka-server.jar
ENTRYPOINT ["java","-jar","/eureka-server.jar"]	
```
3. Configurado el Dockerfile, procederemos a construir la imagen docker, para esto ingrese nuevamente a la carpeta del microservicio y abra la consola, a continuaci贸n ejecute el siguiente comando:
	
```powershell
docker build -t <usuario docker>/<nombre de la imagen>:<tag> .	
```

> NOTA: En ```<usuario docker>``` ingrese el nombre de usuario docker que tiene en docker desktop. Para ```<nombre de la imagen>``` ingrese el nombre del microservicio, en este caso "servicio-eureka-server". Y por 煤ltimo ingrese el ```<tag>``` que identifica la versi贸n de la imagen, en este caso v1. 

<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/build.gif"></p>

4. Una vez construida la imagen, puede probarla localmente, para verificar que funciona correctamente. Para esto, ejecute el siguiente comando:
	
```powershell
docker run -p 8761:8761 --name servicio-eureka-server --network springcloud <usuario docker>/servicio-eureka-server:v1
```	
> NOTA: Si se va a probar localmente todo el proyecto, se recomienda correr el servicio con un --name especifico y adem谩s en una network especifica (Para crear esta network ejecute el comando ```docker network create springcloud```)

<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/run.gif"></p>
	
5. Ahora debemos publicar la imagen construida en DockerHub, para esto ejecute el siguiente comando:
	
```powershell
docker push <usuario docker>/servicio-eureka-server:v1
```
<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/push.gif"></p>

6. Compruebe en DockerHub que su imagen ha sido publicada y copie la direcci贸n de la imagen, que debe ser: ```<usuario docker>/servicio-eureka-server:v1```. Ingrese a la consola de Openshift y al proyecto donde ha desplegado la base de datos, aseg煤rese de estar en el perfil de Developer. De click en ```+Add``` del men煤 lateral  e ingrese en la opci贸n ```Container images```. A continuaci贸n complete lo siguiente:

	
* ```Image name from external registry:``` ```<usuario docker>/servicio-eureka-server:v1```
* ```Runtime:``` openjdk
* ```Application:``` Aseg煤rese que sea el nombre del proyecto donde despleg贸 la base de datos.
* ```Name:``` servicio-eureka-server
* ```Resources:``` Deployment
* ```Advanced options:``` Seleccione la opci贸n Create a route to the Application.
	
Por 煤ltimo de click en ```Create```.
	
<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/openshift.gif"></p>
	
7. Una vez desplegado el microservicio, de click en la ruta creada para acceder al microservicio, debe acceder a la consola convecional de Eureka.

<p align="center"><img width="700" src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/eureka.gif"></p>
<br />
	
## Configuraci贸n y despliegue de microservicios :paperclips:
Una vez ha verificado el funcionamiento de Eureka, el paso siguiente consiste en configurar y desplegar los microservicios usuarios, cursos, examenes y respuestas. Los pasos que se indican a continuaci贸n los debe repetir para cada microservicio establecido, modificando las caracter铆sticas indicadas. Realice lo siguiente:

1. En el ```application.properties``` de cada microservicio aseg煤rese de completar las siguientes modificaciones, teniendo en cuenta el servicio de eureka y la conexi贸n con la base de datos:

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
	
	> NOTA: recuerde reemplazar ```<usuario docker>``` con el nombre de usuario docker que tiene en docker desktop, ```<nombre de la imagen>``` con el nombre de cada microservicio (servicio-usuarios, servicio-cursos, servicio-examenes y servicio-respuestas) y por 煤ltimo ingrese el ```<tag>``` que identifica la versi贸n de la imagen, en este caso v1.
	
5. Cuando finalice el proceso de creaci贸n de la imagen de cada microservicio, realice pruebas localmente con el comando:
	
	```powershell
	docker run -p <puerto>:<puerto> --name <nombre de la imagen> --network springcloud <usuario docker>/<nombre de la imagen>:v1
	```
	
	> NOTA: recuerde reemplazar el valor de ```<puerto>``` con el puerto indicado para cada microservicio, ```<nombre de la imagen>``` con el nombre del microservicio respectivo (servicio-usuarios, servicio-cursos, servicio-examenes y servicio-respuestas) y ```<usuario docker>``` con el nombre de usuario docker que tiene en docker desktop.
																		 
6. Publique las im谩genes construidas en DockerHub, para esto ejecute el siguiente comando:
	
	```powershell
	docker push <usuario docker>/<nombre de la imagen>:v1
	```
	
7. Verifique que cada una de las im谩genes se ha publicado en DockerHub.
	
8. Despliegue la imagen de cada microservicio en la consola web de OpenShift. Aseg煤rese de estar en el proyecto donde tiene desplegada la base de datos y el servicio Eureka con el rol de Developer. Luego realice lo siguiente:
												
	* De click en la pesta帽a ```+Add``` (ubicada en el men煤 lateral izquierdo) e ingrese en la opci贸n ```Container images```.
																		 
	En los siguientes campos complete:
	* ```Image name from external registry```: ```<usuario docker>/<nombre de la imagen>:v1```. Para cada microservicio coloque:
		* ```<usuario docker>/servicio-usuarios:v1```
		* ```<usuario docker>/servicio-cursos:v1```
		* ```<usuario docker>/servicio-examenes:v1```
		* ```<usuario docker>/servicio-respuestas:v1```
	
	* ```Runtime```: openjdk
	* ```Application```: Aseg煤rese que sea el nombre del proyecto donde despleg贸 la base de datos y el servicio Eureka.
	* ```Name```: nombre-servicio
	  Para cada microservicio coloque:
		* servicio-usuarios
		* servicio-cursos
		* servicio-examenes
		* servicio-respuestas
	
	* ```Resources```: Deployment.
	* ```Advanced options```: Seleccione la opci贸n *Create a route to the Application*.
	
	Por 煤ltimo de click en el bot贸n ```Create```.
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/DesplegarUsuarios.gif"></p>

9. Una vez desplegado cada uno de los microservicios, verifique que se hayan registrado en Eureka. Posteriormente, de click en la ruta creada para acceder a cada microservicio (todos deben responder con [ ] si no hay ning煤n dato. El 煤nico microservicio que no entrega respuesta es el servicio-respuestas). 
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/Eureka_microservicios.PNG"></p>
	
	<br />
	
	Realice la prueba con cada microservicio desplegado.
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/ProbarUsuarios.gif"></p>
																		 
<br />
	
## Configuraci贸n y despliegue del microservicio Gateway :door:
Luego de verificar el funcionamiento de los respectivos microservicios, el paso siguiente consiste en desplegar el microservicio gateway. Spring Cloud Gateway funciona como punto de entrada a los microservicios, proporcionando caracter铆sticas y capacidades como: enrutamiento din谩mico, seguridad y monitoreo de solicitudes y llamadas que se realizan. Complete los pasos que se muestran a continuaci贸n para configurar y desplegar este microservicio:
	
1. En el ```application.properties``` del microservicio indique las rutas URL de los microservicios usuarios, cursos, examenes y respuestas, obtenidas en el paso 8 de la secci贸n [Configuraci贸n y despliegue de microservicios](#Configuraci贸n-y-despliegue-de-microservicios-paperclips). Para esto reemplace los valores ```url_servicio_usuarios```, ```url_servicio_cursos```, ```url_servicio_examenes``` y ```url_servicio_respuestas```. Por otro lado, el puerto por defecto para este microservicio en ```8090```.

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
	
	> NOTA: recuerde reemplazar ```<usuario docker>``` con el nombre del usuario docker que tiene en docker desktop, ```<nombre de la imagen>``` con el nombre del microservicio (servicio-gateway) y por 煤ltimo ingrese el ```<tag>``` que identifica la versi贸n de la imagen, en este caso v1.
	
5. Cuando finalice el proceso de creaci贸n de la imagen del microservicio gateway, realice pruebas localmente con el comando:
	
	```powershell
	docker run -p <puerto>:<puerto> --name <nombre de la imagen> --network springcloud <usuario docker>/<nombre de la imagen>:v1
	```
	
	> NOTA: recuerde reemplazar el valor de ```<puerto>``` con el puerto indicado para el microservicio, ```<nombre de la imagen>``` con el nombre del microservicio (servicio-gateway) y ```<usuario docker>``` con el nombre del usuario docker que tiene en docker desktop.
																		 
6. Publique la imagen construida en DockerHub, para esto ejecute el siguiente comando:
	
	```powershell
	docker push <usuario docker>/<nombre de la imagen>:v1
	```
	
7. Verifique que la imagen se ha publicado en DockerHub.
	
8. Despliegue la imagen del microservicio gateway en la consola web de OpenShift. Aseg煤rese de estar en el proyecto donde tiene desplegada la base de datos,  el servicio Eureka y los dem谩s microservicios con el rol de Developer. Luego realice lo siguiente:
												
	* De click en la pesta帽a ```+Add``` (ubicada en el men煤 lateral izquierdo) e ingrese en la opci贸n ```Container images```.
																		 
	En los siguientes campos complete:
	* ```Image name from external registry```: ```<usuario docker>/<nombre de la imagen>:v1```. Para este caso coloque ```<usuario docker>/servicio-gateway:v1```.		
	* ```Runtime```: openjdk
	* ```Application```: Aseg煤rese que sea el nombre del proyecto donde despleg贸 la base de datos, el servicio Eureka y los dem谩s microservicios.
	* ```Name```: servicio-gateway.	
	* ```Resources```: Deployment.
	* ```Advanced options```: Seleccione la opci贸n *Create a route to the Application*.
	
	Por 煤ltimo de click en el bot贸n ```Create```.
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/DesplegarGateway.gif"></p>

9. Una vez desplegado el microservicio, verifique que se haya registrado en Eureka. Luego, de click en la ruta creada para acceder al mismo. Una vez cargue la URL no observar谩 respuesta, por lo que debe agregar al final de la ruta cada path de acuerdo al microservicio, ejemplo: ```/api/alumnos```, ```/api/cursos```, ```/api/examenes``` y ```/api/respuestas```. 
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/Eureka_gateway.PNG"></p>
	
	<br />
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/ProbarGateway.gif"></p>
	
	
<br />

## Configuraci贸n del frontend en Angular :a: 
	
1. Para crear su proyecto en angular y conectarlo al backend que ya se encuentra desplegado en OpenShift siga estos pasos:
	
	
	- Para representar cada una de las clases y sus atributos existen los componentes models. Cree una carpeta llamada models y en ella cree cada uno de los modelos:  		alumno, curso, examen, pregunta y respuesta:
	
	```
	ng g class models/<nombre del modelo> --skipTests=true
	```
	
	A continuaci贸n se muestra a manera de ejemplo el formato que se sigue para la clase alumno.ts:
	
	```
	export class Alumno{
	id: number;
	nombre: string;
	apellido: string;
	email: string;
	createAt: string; 
	fotoHashCode: number;}
	```
	
	- Mediante los componentes services, Angular se podr谩 conectar al backend y obtener datos de este. Cree una carpeta llamada services y en ella cree cada uno de los servicios: alumno, curso, examen y respuesta:
	
	
	```
		ng g service services/<nombre del servicio> --skipTests=true
	```

	
	A continuaci贸n se muestra a manera de ejemplo el formato que se sigue para el servicio alumno.service.ts:
	
	```
	import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
	import { Injectable } from '@angular/core';
	import { Observable } from 'rxjs';
	import { Alumno } from '../models/alumno';

	@Injectable({
	  providedIn: 'root'
	})
	export class AlumnoService {

	  private baseEndpoint = '<Ruta del servicio backend>';
	  private cabeceras: HttpHeaders = new HttpHeaders({'Content-Type': 'application/json'});
	  constructor(private http: HttpClient) { }

	  public listar(): Observable<Alumno[]> {
	    return this.http.get<Alumno[]>(this.baseEndpoint);
	  }

	  public listarPaginas(page: string, size: string): Observable<any>{
	    const params = new HttpParams()
	    .set('page', page)
	    .set('size', size);
	    return this.http.get<any>(`${this.baseEndpoint}/pagina`, {params: params});
	  }

	  public ver(id: number): Observable<Alumno>{
	    return this.http.get<Alumno>(`${this.baseEndpoint}/${id}`);
	  }

	  public crear(alumno:Alumno): Observable<Alumno>{
	    return this.http.post<Alumno>(this.baseEndpoint, alumno, { headers: this.cabeceras});
	  }

	  public editar(alumno:Alumno): Observable<Alumno>{
	    return this.http.put<Alumno>(`${this.baseEndpoint}/${alumno.id}`, { headers: this.cabeceras});
	  }

	  public eliminar(id: number): Observable<void>{
	    return this.http.delete<void>(`${this.baseEndpoint}/${id}`);}}
	``` 
	
	> NOTA: En el string baseEndpoint ingrese la ruta entragada por Openshift para su servicio de Alumnos.
	
- Cree el componente de los microservicios, que finalmente ser谩 lo que visualice en la p谩gina web:
	
	```
	ng g component components/<Nombre del componente>
	```
	
	A continuaci贸n se muestra a manera de ejemplo el formato que se sigue para el componente alumnos.components.ts:
	
	```
	import { Component, OnInit } from '@angular/core';
	import { Alumno } from 'src/app/models/alumno';
	import { AlumnoService } from 'src/app/services/alumno.service';

	@Component({
	  selector: 'app-alumnos',
	  templateUrl: './alumnos.component.html',
	  styleUrls: ['./alumnos.component.css']
	})
	export class AlumnosComponent implements OnInit {

	  titulo= 'Listado de alumnos';
	  alumnos: Alumno[];
	  constructor(private service: AlumnoService ) { }

	  ngOnInit(){
	    this.service.listar().subscribe(alumnos => {
		this.alumnos= alumnos;
	    });}}	
	```

Despues de tener estos componentes base, podr谩 empezar a crear distintos componentes que le generen las diferentes vistas para cada servicio, por ejemplo para el microservicio alumnos se creo el componente ```alumnos-form.components.ts```. En este componente se agrego el siguiente c贸digo:
	
	```
	import { Component, OnInit } from '@angular/core';
	import { ActivatedRoute, Router } from '@angular/router';
	import { Alumno } from 'src/app/models/alumno';
	import { AlumnoService } from 'src/app/services/alumno.service';

	@Component({
	  selector: 'app-alumnos-form',
	  templateUrl: './alumnos-form.component.html',
	  styleUrls: ['./alumnos-form.component.css']
	})
	export class AlumnosFormComponent implements OnInit {
	  titulo= 'Crear alumnos';

	  alumno: Alumno= new Alumno();
	  error: any;
	  constructor(private service: AlumnoService,
	     private router: Router,
	     private route: ActivatedRoute ) { }



	  ngOnInit(){
	    this.route.paramMap.subscribe(params => {
	      const id: number = +params.get('id');
	      if(id){
		this.service.ver(id).subscribe(alumno => 
		  this.alumno= alumno)
	      }
	    })
	  }

	  public crear():void {
	    console.log(this.alumno);
	    this.service.crear(this.alumno).subscribe(alumno =>{
	      console.log(alumno);
	      alert(`Alumno ${alumno.nombre} creado con 茅xito`);
	      this.router.navigate(["/alumnos"]);
	  }, err => {
	    if(err.status === 400){
	      this.error = err.error;
	      console.log(this.error);
	    }
	  })
	}

	public editar(): void {
	  console.log(this.alumno);
	  this.service.editar(this.alumno).subscribe(alumno =>{
	    console.log(alumno);
	    alert(`Alumno ${alumno.nombre} actualizado con 茅xito`);
	    this.router.navigate(["/alumnos"]);
	}, err => {
	  if(err.status === 400){
	    this.error = err.error;
	    console.log(this.error);
	  }
	})
	}

	}
	```
	
Donde se evidencia que se realiza la subscripci贸n al servicio alumnos creado anteriormente, para asi crear dos m茅todos crear y editar que se conectar谩n al backend de la aplicaci贸n. Finalmente en el html de este componente se crea una lista y se llaman ambos m茅todos para permitir al usuario crear o editar un alumno.
	
```
	<div class="card text-dark bg-light mb-3">
    <div class="card-header">{{titulo}}</div>
    <div class="card-body">
        <button class="btn btn-secondary my-2" routerLink="/alumnos">volver</button>

        <form>
            <div class="form-group">
                <label for="nombre">Nombre</label>
                <input type="text" name="nombre" class="form-control" id="nombre"
                [(ngModel)]="alumno.nombre">
                <div class="alert alert-danger" *ngIf="error?.nombre">
                    {{ error.nombre }}
                </div>
            </div>
            <div class="form-group">
                <label for="apellido">Apellido</label>
                <input type="text" name="apellido" class="form-control" id="apellido"
                [(ngModel)]="alumno.apellido">
                <div class="alert alert-danger" *ngIf="error?.apellido">
                    {{ error.apellido }}
                </div>
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="text" name="email" class="form-control" id="email"
                [(ngModel)]="alumno.email">
                <div class="alert alert-danger" *ngIf="error?.email">
                    {{ error.email }}
                </div>
            </div>
            <div class="form-group">
                <button type="button" class="btn btn-primary" (click)="alumno.id? editar() : crear()">{{ alumno.id? 'Editar' : 'Crear' }}</button>
            </div>
        </form>
</div>
</div>
```
<br />
	
## Creaci贸n de imagen frontend
Para crear la imagen del frontend de la aplicacion realice lo siguiente:

1. Clone el repositorio con el comando ```git clone https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop``` en su computador y luego acceda a la carpeta del frontend en una ventana de Windows *PowerShell*.

2. El Dockerfile ya se encuentra listo. Procederemos a construir la imagen docker con el siguiente comando:
	
```powershell
docker build -t <usuario docker>/<nombre de la imagen>:<tag> .	
```

> NOTA: En ```<usuario docker>``` ingrese el nombre de usuario docker que tiene en docker desktop. Para ```<nombre de la imagen>``` un nombre exclusivo para identificar la imagen. Por 煤ltimo ingrese el ```<tag>``` que identifica la versi贸n de la imagen, en este caso v1. 


3. Una vez construida la imagen puede probarla localmente, para verificar que funciona correctamente. Para esto, ejecute el siguiente comando:
	
```powershell
docker run -p 8085:8080 <usuario docker>/<nombre de la imagen>:<tag>
```	
	
4. Ahora debemos publicar la imagen construida en DockerHub, para esto ejecute el siguiente comando:
	
```powershell
docker push <usuario docker>/<nombre de la imagen>:<tag>
```
	
5. Verifique que la imagen se ha publicado en DockerHub.
<br />
	
## Despliegue de frontend en OpenShift
Para desplegar la imagen en OpenShift dentro del mismo proyecto en donde despleg贸 el backend, realice lo siguiente:
	
1.  De click en la pesta帽a ```+Add``` (ubicada en el men煤 lateral izquierdo) e ingrese en la opci贸n ```Container images```.
																		 
	En los siguientes campos complete:
	* ```Image name from external registry```: ```<usuario docker>/<nombre de la imagen>:<tag>```. 		
	* ```Runtime```: angularjs
	* ```Application```: Cree una nueva aplicaci贸n para con el nombre ```frontend```.
	* ```Name```: frontend-microservicios.	
	* ```Resources```: Deployment.
	* ```Advanced options```: Seleccione la opci贸n *Create a route to the Application*.
	
	Por 煤ltimo de click en el bot贸n ```Create```.
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/Frontend.gif"></p>
	
	<br />

2. Verifique que se ha desplegado el frontend de la aplicaci贸n. Luego, de click en la ruta creada para acceder al mismo. Una vez cargue la URL observar谩 que salen 3 pesta帽as para explorar sobre Alumnos, Cursos y Ex谩menes. 
	
	<p align="center"><img src="https://github.com/emeloibmco/Microservicios-Spring-Boot-Workshop/blob/main/Images/PruebaFrontend.gif"></p>
	
	<br />
	
	
<br />
<br />

## NOTAS sobre temas pendientes con el frontend
* Pendiente solucionar problema con la operaci贸n ```Editar``` en Alumnos.
* Pendiente agregar la operaci贸n ```Eliminar``` en Alumnos.
* Pendiente la configuraci贸n del frontend para los componentes Cursos y Examenes.
<br />
	
## Referencias :book:
* Curso de Udemy <a href="https://ibm-learning.udemy.com/course/microservicios-spring-cloud-y-angular-9/learn/lecture/17302890#questions">- Microservicios con Spring Cloud y Angular full stack</a>. C贸digo de la aplicaci贸n y configuraci贸n del backend y frontend.
* Curso de Udemy <a href="https://ibm-learning.udemy.com/course/microservicios-con-spring-boot-y-spring-cloud/learn/lecture/15365312?start=0#overview">- Microservicios con Spring Boot y Spring Cloud Netflix Eureka</a>.
<br />

## Autores :black_nib:
Equipo IBM Cloud Tech Sales Colombia.
<br />
