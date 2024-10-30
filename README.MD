Backend de Clínica - Sistema de Información
Este proyecto es una API REST desarrollada en Spring Boot para gestionar la información de pacientes y otros recursos de una clínica. Proporciona endpoints para registrar, autenticar y gestionar información de pacientes, así como otros módulos clínicos, utilizando una base de datos PostgreSQL.

Tabla de Contenidos
Características
Tecnologías Utilizadas
Requisitos Previos
Instalación y Configuración
Estructura del Proyecto
Ejemplo de Peticiones
Ejecución de Pruebas
Contribuciones
Licencia
Características
Registro y autenticación de pacientes.
Gestión de información de pacientes, incluyendo datos personales y seguro médico.
Integración con PostgreSQL para el almacenamiento de datos.
Seguridad y manejo de autenticación utilizando JWT.
Documentación de API (recomendado agregar Swagger en el futuro para facilitar la documentación de endpoints).
Tecnologías Utilizadas
Java 17 - Lenguaje de programación.
Spring Boot - Framework principal para construir la API.
Spring Data JPA - Para la interacción con la base de datos.
PostgreSQL - Base de datos relacional.
Maven - Herramienta de gestión de dependencias y construcción del proyecto.
Lombok - Para reducir el código repetitivo de getters, setters, constructores, etc.
JWT - Para la autenticación segura de usuarios.
Requisitos Previos
Antes de comenzar, asegúrate de tener lo siguiente instalado en tu sistema:

Java 17 o superior.
Maven.
PostgreSQL.
Postman o alguna herramienta similar para probar la API.
Instalación y Configuración
1. Clona el repositorio
git clone https://github.com/tuusuario/backendclinic.git
cd backendclinic
2. Configura la Base de Datos
Crea una base de datos en PostgreSQL:
CREATE DATABASE bd_historial_clinico;
Actualiza las credenciales de la base de datos en el archivo src/main/resources/application.properties:
spring.datasource.url=jdbc:postgresql://<TU_HOST>:5432/bd_historial_clinico
spring.datasource.username=<TU_USUARIO>
spring.datasource.password=<TU_CONTRASEÑA>
3. Compila y Ejecuta el Proyecto
En la terminal, dentro del directorio del proyecto, ejecuta:
mvn clean install
mvn spring-boot:run
Esto iniciará el servidor en http://localhost:8080.
Estructura del Proyecto
backendclinic/
├── src/main/java/app/backendclinic
│   ├── auth              # Contiene clases relacionadas con la autenticación y registro
│   ├── controllers       # Controladores de la API
│   ├── models            # Entidades del modelo de datos
│   ├── repositorys       # Repositorios para interactuar con la base de datos
│   ├── services          # Lógica de negocio y servicios
│   └── jwt               # Servicios relacionados con JWT para la autenticación
├── src/main/resources
│   └── application.properties # Configuración de la base de datos y de la aplicación
└── pom.xml               # Archivo de dependencias y configuración de Maven