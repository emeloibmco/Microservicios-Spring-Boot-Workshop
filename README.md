# Microservicios Spring-Boot Workshop :leaves::cloud:

<br />

## Índice  📰
1. [Pre-Requisitos](#pre-requisitos-pencil)
2. [Librerias commons](#Librerias-commons-books)
3. [Desplegar la aplicación en Code Engine](#Desplegar-la-aplicación-en-Code-Engine-arrow_double_down)
4. [Acceder a la aplicación](#Acceder-a-la-aplicación-computer)
5. [Referencias](#Referencias-book)
6. [Autores](#Autores-black_nib)

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

```<Nombre del paquete>/<Nombre del proyecto>/<Version>````

Por ejemplo, el jar de la libreria commons-alumnos quedo incluida en la siguiente direccion com/formacion/springboot/commons/alumnos/commons-alumnos/0.0.1-SNAPSHOT/. Revise como quedaron configuradas las carpetas en el repositorio de librerias commons creado para este [proyecto](https://github.com/paulapachon/mvn-repo).
  
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
    
 Recuerde incluirlo en todos los miscroservicios que hagan uso de estas librerias commons.

<br />


## Referencias :book:
<br />

## Autores :black_nib:
Equipo IBM Cloud Tech Sales Colombia.
<br />
