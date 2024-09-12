## Govench-Back

## Introducción
**Govench** es una aplicación web diseñada para promover y facilitar tanto la gestion registro de eventos. Con Govench, los usuarios pueden crear su cuenta, iniciar sesión, realizar y pagos. 

La aplicación permitirá crear, compartir y participar en eventos y comunidades relacionados al tema tegnológico. A su vez podremos visualizar dinámicamente los eventos registrados o creados y recibir notificaciones.


Govench es una plataforma web vibrante y accesible que transforma la manera en que interactúas con el mundo de la tecnología. Te ofrece la capacidad de crear y gestionar eventos, mantener un calendario personalizado, y participar en comunidades apasionadas por la innovación. Con Govench, cada usuario puede visualizar eventos, recibir notificaciones en tiempo real, y obtener certificados a través de pagos seguros. Nuestro objetivo es proporcionar un espacio en el que tus intereses tecnológicos sean celebrados, facilitando el aprendizaje y la colaboración en una experiencia en línea enriquecedora y conectada.


### Colaboradores del Proyecto

| **Nombre**     | **Rol**      | **Perfil**        |
|------------|--------------|---------------|
| Acevedo Villena Dylan | Desarrollador | Frontend      |
| Aguilar Blas Javier  | Diseñadora    | UI/UX         |
| Guevara Villalobos Gino| Gerente      | Proyecto      |
| Palomino Cuenca Jaime | Analista     | QA            |
| Escobar Gomez Miguel| Administrador| Sistemas      |

Aqui mismo puede visualizar el avance de nuestro trabajo [Tablero de Jira] (https://upao-team-rdkenqgm.atlassian.net/jira/software/projects/GOV/boards/67/backlog?atlOrigin=eyJpIjoiODg2NmY2ZWVjMThkNDJlNmJhZDk0MmJjMWYxMjFlNWYiLCJwIjoiaiJ9).

### Funcionalidades de Govench

#### **Módulo de Gestión de Eventos**

- **Historia:**
    - funcion.

#### **Módulo de Gestión de Usuarios**

- **Creación de Usuarios e Inicio de Sesión:**
    - Permitir a los usuarios registrarse en la plataforma.

#### **Gestión de Comunidades**

- **Creación de Usuarios e Inicio de Sesión:**
    - Permitir a los usuarios registrarse en la plataforma.

#### **Gestión de Articulos**

- **Creación de Usuarios e Inicio de Sesión:**
    - Permitir a los usuarios registrarse en la plataforma.

#### **Módulo de Inscripción y Participación**

- **Creación de Usuarios e Inicio de Sesión:**
    - Permitir a los usuarios registrarse en la plataforma.

#### **Pagos en Linea**

- **Creación de Usuarios e Inicio de Sesión:**
    - Permitir a los usuarios registrarse en la plataforma.

#### **Gestión y emmisión de reportes**

- **Creación de Usuarios e Inicio de Sesión:**
    - Permitir a los usuarios registrarse en la plataforma.



### Diagramas de aplicacion

Para entender mejor la estructura y diseño de la aplicación "Govench", revisa los siguientes Diagramas
### Diagrama de Clases

![Diagrama de Clases](diagrama_clase_venta_libro.png)
### Diagrama de Base de Datos

![Diagrama de Base de Datos]("src")

Este diagrama ilustra el esquema de la base de datos utilizada por la aplicación, mostrando las tablas, columnas, y relaciones entre las entidades.

### Descripción de Capas del Proyecto

| capa        | descripción                                                                                  |
|-------------|----------------------------------------------------------------------------------------------|
| api         | Contiene los controladores REST que manejan las solicitudes HTTP y las respuestas.            |
| entity      | Define las entidades del modelo de datos que se mapean a las tablas de la base de datos.      |
| repository  | Proporciona la interfaz para las operaciones CRUD y la interacción con la base de datos.      |
| service     | Declara la lógica de negocio y las operaciones que se realizarán sobre las entidades.         |
| service impl| Implementa la lógica de negocio definida en los servicios, utilizando los repositorios necesarios. |
