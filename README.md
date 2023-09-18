# Challenge Backend GlobalLogic

Esta app desarrollada en Spring expone una API con dos endpoint /api/v1/auth/register que recibe un usuario para registrar y que devuelve entre otros datos el token JWT implementado con Spring Security para ser usado en el endpoint /api/v1/auth/login que con solo enviarle el token devuelve los datos del usuario y un nuevo token de logueo.

## 🔨Desarrollado con:
* [Java 1.8](https://www.oracle.com/ar/java/technologies/javase/javase8-archive-downloads.html)
* [Gradle](https://gradle.org/) Para manejar las dependencias.
* [Spring Boot](https://spring.io/projects/spring-boot)
* [H2](https://h2database.com/html/main.html)

## 👷Colección Postman
Json para importar a [Postman](https://www.postman.com/) para el testeo de la API:
[Localhost](https://github.com/KernelPanic22/challenge-jwt-globallogic/blob/master/GlobalLogic.postman_collection.json)

## 📝Deployment
* La aplicación utiliza el puerto 8083 si desea cambiarlo puede hacerlo desde el archivo application.yml en la carpeta src/main/resources.
* Se utiliza un archivo .env para guardar la variable de entorno SECRET_KEY que es la que se utiliza para firmar el token JWT.
  Se debe crear dicho archivo en src/main/resources y agregar la variable de entorno de la siguiente manera:
```
SECRET_KEY= "clave secreta"
```
La clave puede ser generada en la siguiente [web](https://generate-random.org/encryption-key-generator)

* Para correr la aplicación se debe ejecutar el siguiente comando en la carpeta raíz del proyecto (una vez bajadas todas las dependencias y tener configurado el jdk a java 1.8):
```
gradle bootRun
```

## 📄Documentación
El reporte de Jacoco puede ser encontrado dentro de la carpeta build/reports/jacoco/test/html/index.html
Para correr los test de Jacoco se debe ejecutar el siguiente comando en la carpeta raíz del proyecto posteriormente al build con gradle:
```
gradle build
gradle test jacocoTestReport
```

## 📌Endpoints
* /api/v1/auth/register
* /api/v1/auth/login

## 📦Estructura del proyecto
```
├───.main
│   ├───java
│   │   └───com
│   │       └───globallogic
│   │           └───challenge
│   │               ├───config
│   │               ├───controller
│   │               ├───dto
│   │               ├───exception
│   │               ├───model
│   │               ├───repository
│   │               └───service
│   └───resources

```
* En el paquete config se encuentra la configuración de Spring Security.
* En el paquete controller se encuentran los controladores de la API.
* En el paquete dto se encuentran los objetos de transferencia de datos.
* En el paquete exception se encuentran las excepciones personalizadas.
* En el paquete model se encuentran los modelos de la base de datos.
* En el paquete repository se encuentran los repositorios de la base de datos.
* En el paquete service se encuentran los servicios de la API.
* En el paquete resources se encuentra el archivo application.yml con la configuración de la aplicación.

## UML
![UML](https://github.com/KernelPanic22/challenge-jwt-globallogic/blob/b8608648f33973ca22bccef8814a4dcf27fab703/src/main/java/com/GlobalLogic/security/Class%20Diagram.png)







