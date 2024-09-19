
# Introducción
**Govench** es una aplicación web diseñada para promover y facilitar tanto la gestion registro de eventos. Con Govench, los usuarios pueden crear su cuenta, iniciar sesión, realizar y pagos. 

La aplicación permitirá crear, compartir y participar en eventos y comunidades relacionados al tema tegnológico. A su vez podremos visualizar dinámicamente los eventos registrados o creados y recibir notificaciones.


Govench es una plataforma web vibrante y accesible que transforma la manera en que interactúas con el mundo de la tecnología. Te ofrece la capacidad de crear y gestionar eventos, mantener un calendario personalizado, y participar en comunidades apasionadas por la innovación. Con Govench, cada usuario puede visualizar eventos, recibir notificaciones en tiempo real, y obtener certificados a través de pagos seguros. Nuestro objetivo es proporcionar un espacio en el que tus intereses tecnológicos sean celebrados, facilitando el aprendizaje y la colaboración en una experiencia en línea enriquecedora y conectada.


### Colaboradores del Proyecto

| **Nombre**           | **Rol**                                | **Perfil**                  |
|----------------------|----------------------------------------|-----------------------------|
| Javier Aguilar        | Líder del Proyecto y Backend Developer | [LinkedIn/Javier Aguilar]()  |
| Dylan Acevedo         | Base de Datos y Backend Developer      | [LinkedIn/Dylan Acevedo]()   |
| Miguel Escobar        | Seguridad y QA                        | [LinkedIn/Miguel Escobar]()  |
| Gino Guevara          | Diseño y Frontend Developer            | [LinkedIn/Gino Guevara]()    |
| Jaime Palomino        | Frontend Developer                    | [LinkedIn/Jaime Palomino]()  |


### Revisa el Progreso del Proyecto BookHub

| **Columna**       | **Descripción**                                                                                                                                    |
|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| **Backlog**       | Contiene todas las historias de usuario, tareas y características que deben desarrollarse. Es el listado de todo el trabajo pendiente.              |
| **En Progreso**   | Incluye las tareas que están actualmente en desarrollo. Visualiza el trabajo en curso para asegurar el flujo continuo de trabajo.                   |
| **Revisión**      | Después de completar una tarea, se mueve aquí para una revisión de código y revisión por pares (peer review). Esta fase incluye la creación de **pull requests** para asegurar que el código cumpla con los estándares de calidad antes de integrarse al proyecto principal. |
| **En Pruebas**    | Contiene las tareas que han pasado la revisión de código y necesitan pruebas exhaustivas (unitarias, de integración y de aceptación) para garantizar su calidad. |
| **Hecho**         | Las tareas completamente desarrolladas, revisadas y probadas se mueven aquí, indicando que están listas y finalizadas.                               |

Mira cómo va avanzando nuestro trabajo visitando el siguiente enlace: [Tablero de Trello](https://trello.com/b/5sNtLdze).


### Funcionalidades de la Aplicación Govench

#### 1. **Módulo de Gestión de Eventos**

Este módulo se enfoca en la creación y gestión de eventos tecnológicos:

- **Crear evento:** Los usuarios pueden crear un evento nuevo con detalles específicos.
- **Buscar eventos por nivel de experiencia:** Facilita la búsqueda de eventos según la experiencia del usuario.
- **Cancelar participación:** Los usuarios pueden cancelar su participación en eventos antes de su inicio.
- **Gestionar lista de participantes:** Los organizadores pueden administrar la lista de participantes para cada evento.

#### 2. **Módulo de Gestión de Usuarios**

Este módulo está orientado a la administración de cuentas de usuario y sus interacciones:

- **Crear una cuenta:** Registro de nuevos usuarios.
- **Iniciar sesión:** Acceso a la plataforma con credenciales.
- **Recuperar contraseña:** Funcionalidad para recuperar el acceso en caso de pérdida de contraseña.
- **Administrar perfiles:** Actualización de los detalles de usuario.
- **Seguir a usuarios:** Opción de seguir a otros usuarios dentro de la plataforma.

#### 3. **Gestión de Comunidades**

Este módulo permite a los usuarios crear y participar en comunidades dentro de la plataforma:

- **Unirse a comunidades:** Los usuarios pueden unirse a las comunidades de su interés.
- **Crear comunidades:** Funcionalidad para que los usuarios creen nuevas comunidades en torno a temáticas específicas.
- **Publicar contenido:** Los usuarios pueden compartir información y publicaciones dentro de las comunidades.

#### 4. **Módulo de Inscripción y Participación**

Permite a los usuarios gestionar sus inscripciones en los eventos disponibles:

- **Registrarse en eventos:** Funcionalidad para inscribirse en eventos.
- **Proporcionar comentarios sobre eventos:** Los usuarios pueden dar retroalimentación sobre los eventos en los que han participado.
- **Gestionar pagos para eventos premium:** Se implementa la compra de entradas a eventos a través de una pasarela de pago segura (PayPal).

#### 5. **Pagos en Línea**

Este módulo gestiona el proceso de pagos para los eventos premium:

- **Integración con PayPal:** Proporciona una experiencia de pago segura y eficiente para eventos premium.

#### 6. **Reportes y Estadísticas**

Módulo que proporciona a los usuarios acceso a estadísticas e información sobre sus actividades:

- **Generar reportes de actividad:** Se pueden generar reportes detallados sobre la actividad de los usuarios y su participación en eventos.
- **Visualización de estadísticas:** Los usuarios pueden ver estadísticas sobre su participación en eventos y su interacción con la plataforma.

## Diagramas de la Aplicación

Para entender mejor la estructura y diseño de la aplicación "BookHub", revisa los siguientes diagramas:

### Diagrama de Clases

![Diagrama de Clases](img/Diagrama%20Clases%20-%20Transa.jpeg)


### Diagrama de Base de Datos

![Diagrama de Base de Datos](img/Diagrama%20Base%20de%20datos.jpeg)

Este diagrama ilustra el esquema de la base de datos utilizada por la aplicación, mostrando las tablas, columnas, y relaciones entre las entidades.

### Descripción de Capas del Proyecto

| capa        | descripción                                                                                  |
|-------------|----------------------------------------------------------------------------------------------|
| api         | Contiene los controladores REST que manejan las solicitudes HTTP y las respuestas.            |
| entity      | Define las entidades del modelo de datos que se mapean a las tablas de la base de datos.      |
| repository  | Proporciona la interfaz para las operaciones CRUD y la interacción con la base de datos.      |
| service     | Declara la lógica de negocio y las operaciones que se realizarán sobre las entidades.         |
| service impl| Implementa la lógica de negocio definida en los servicios, utilizando los repositorios necesarios. |

