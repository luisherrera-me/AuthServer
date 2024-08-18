# AuthServer

![Texto alternativo](https://miro.medium.com/v2/resize:fit:1400/1*it0DDCcRAJwHvXhcTFFvGQ.png)

## Descripción

Este proyecto es un microservicio diseñado para gestionar la autenticación, registro, edición y eliminación de usuarios. Ha sido desarrollado utilizando Kotlin y el framework Ktor, lo que permite una integración eficiente con otros servicios y facilita la implementación de rutas seguras para las operaciones de usuario. Además, el microservicio está optimizado para ofrecer un rendimiento escalable y confiable, asegurando que las transacciones relacionadas con la gestión de usuarios se realicen de manera rápida y segura.

## Visuales

No disponible

## Empezando 🚀


### Prerrequisitos 📋

Lista de software y herramientas, incluyendo versiones, que necesitas para instalar y ejecutar este proyecto:

- IDE (Intellij IDEA)
- Lenguaje de programación (Kotlin)
- Base de datos (MongoDB)

### Configuración 🔧

Para empezar a utilizar el proyecto, es fundamental definir dos variables de entorno cruciales para su correcto funcionamiento. La primera variable especifica la ruta y las credenciales de MongoDB, mientras que la segunda define la clave secreta utilizada para la generación de JWT. A continuación, se detalla la configuración de MongoDB y la clave secreta necesaria.

```bash
MONGODB_URI=mongodb://localhost:27017;
SECRET=akJ9lZ8gHr7T2xLmC4wDfE6qW1pYbV3uN0oQxRzU5tSvO2iJ
```

### ruta de inicio de sesión
```bash
http://127.0.0.1:8080/api/v1/auth/sign_in
```
### JSON
```json
{
    "emailAddress": "luisda99@gmail.com",
    "password": "12345678"
}
```

### Ruta de registro
```bash
http://127.0.0.1:8080/api/v1/auth/sign_up
```
### JSON
```json
{
    "name": "Tu nombre",
    "lastName": "Tu Apellido",
    "phone": "+57000000000",
    "emailAddress": "tu@email.com",
    "password": "TuPassword"
}
```

Y así sucesivamente...

```bash
# paso 2
```
