# Secure API

API REST desarrollada con **Spring Boot** que puede ejecutarse localmente o mediante **Docker**.
El proyecto utiliza **Maven** para la gestión de dependencias y compilación.

---

# Tecnologías utilizadas

- Java 21
- Spring Boot
- Maven
- Docker

---

# Estructura del proyecto

```
secure-api
│
├─ src
│  ├─ main
│  │  ├─ java
│  │  └─ resources
│  │
│  └─ test
│
├─ pom.xml
├─ Dockerfile
├─ .gitignore
├─ .dockerignore
└─ README.md
```

---

# Requisitos

Para ejecutar el proyecto necesitas tener instalado:

- Java 21
- Maven
- Docker (opcional, solo si se ejecuta con contenedores)

---

# Compilar el proyecto

Desde la raíz del proyecto ejecutar:

```
mvn clean package
```

Esto generará el archivo:

```
target/secure-api.jar
```

---

# Ejecutar la aplicación localmente

Una vez compilado el proyecto:

```
java -jar target/secure-api.jar
```

La aplicación quedará disponible en:

```
http://localhost:8080
```

---

# Ejecutar la aplicación con Docker

## Construir la imagen

```
docker build -t secure-api:1.0 .
```

## Ejecutar el contenedor

```
docker run -d -p 8080:8080 --name secure-api secure-api:1.0
```

La API quedará disponible en:

```
http://localhost:8080
```

---

# Verificar contenedores en ejecución

```
docker ps
```

---

# Detener el contenedor

```
docker stop secure-api
```

---

# Eliminar el contenedor

```
docker rm secure-api
```

---

# Flujo de despliegue recomendado

1. Clonar el repositorio

```
git clone <url-del-repositorio>
```

2. Entrar al proyecto

```
cd secure-api
```

3. Construir imagen Docker

```
docker build -t secure-api .
```

4. Ejecutar contenedor

```
docker run -p 8080:8080 secure-api
```

---

docker run -d --name --network paunet -p 8089:8080 backend
