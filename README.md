# ⏱️ ClarityTimer Backend

Backend REST API para aplicación Pomodoro con sistema de gamificación mediante puntos canjeables por personajes Sanrio.

## 🎮 Sistema de Gamificación

### Mecánica
- Completa pomodoros → Gana puntos (10 puntos por pomodoro de trabajo)
- Acumula puntos → Canjea personajes Sanrio
- Mantén rachas → Obtén bonus (+10 a 3 días, +20 a 7 días, +50 a 30 días)
- **Nuevos usuarios reciben 600 puntos de bienvenida** 🎁

### Personajes Disponibles
- **Comunes**: 0-150 puntos (Cinnamoroll gratis, Hello Kitty 100, My Melody 120)
- **Raros**: 150-250 puntos (Kuromi 150, Pochacco 200)
- **Épicos**: 250-400 puntos (Keroppi 250, Badtz-Maru 280)
- **Legendarios**: 400-600 puntos (Chococat 400, Gudetama 500, Aggretsuko 600)

## 🚀 Inicio Rápido

### Requisitos Previos
- **Java 17 o superior** - [Descargar JDK](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** - [Descargar Maven](https://maven.apache.org/download.cgi)
- **MySQL 8.0+** - [Descargar MySQL](https://dev.mysql.com/downloads/mysql/)

### Instalación Paso a Paso

#### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/ClarityTimer-backend.git
cd ClarityTimer-backend/ClarityTimer-backend
```

#### 2. Configurar Base de Datos MySQL

**Opción A: Usando MySQL Workbench o línea de comandos**
```sql
CREATE DATABASE claritytimer_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**Opción B: Usando el script de diagnóstico (Windows)**
```powershell
.\diagnostico.ps1
```
Este script verifica automáticamente tu instalación de MySQL y crea la base de datos si no existe.

#### 3. Configurar Credenciales de Base de Datos

Edita el archivo `src/main/resources/application.properties`:

```properties
# Configuración de Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/claritytimer_db?createDatabaseIfNotExist=true
spring.datasource.username=TU_USUARIO_MYSQL
spring.datasource.password=TU_CONTRASEÑA_MYSQL

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Puerto del servidor
server.port=8080
```

**⚠️ Importante:** Reemplaza `TU_USUARIO_MYSQL` y `TU_CONTRASEÑA_MYSQL` con tus credenciales reales de MySQL.

#### 4. Instalar Dependencias
```bash
mvn clean install
```

#### 5. Ejecutar la Aplicación

**Opción A: Usando Maven**
```bash
mvn spring-boot:run
```

**Opción B: Usando el script de ejecución (Windows)**
```powershell
.\run-backend.ps1
```

**Opción C: Ejecutar el JAR compilado**
```bash
java -jar target/ClarityTimer-backend-0.0.1-SNAPSHOT.jar
```

#### 6. Verificar que el Backend está Funcionando

Abre tu navegador y accede a:
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **API Docs JSON**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8080/actuator/health (si está habilitado)

Si ves la interfaz de Swagger, ¡el backend está funcionando correctamente! ✅

## 📖 Documentación de la API (Swagger)

Una vez que el backend esté corriendo, accede a la documentación interactiva de Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

Desde Swagger puedes:
- 📋 Ver todos los endpoints disponibles
- 🧪 Probar las APIs directamente desde el navegador
- 📝 Ver los modelos de datos (DTOs)
- 🔐 Autenticarte con JWT para probar endpoints protegidos

## 📡 Endpoints Principales

### Autenticación (`/api/v1/auth`)
- `POST /register` - Registrar nuevo usuario (recibe 600 puntos + Cinnamoroll gratis)
- `POST /login` - Iniciar sesión
- `GET /me` - Obtener información del usuario actual

### Personajes (`/api/v1/personajes`)
- `GET /` - Ver todos los personajes de la tienda
- `GET /disponibles` - Personajes que puedes comprar con tus puntos
- `GET /desbloqueados` - Mis personajes (inventario)
- `POST /{id}/comprar` - Comprar personaje con puntos
- `PUT /{id}/activar` - Seleccionar personaje activo

### Sesiones (`/api/v1/sesiones`)
- `POST /{id}/completar` - Completar sesión (otorga puntos automáticamente)

### Estadísticas (`/api/v1/estadisticas`)
- `GET /puntos/historial` - Historial de transacciones
- `GET /mi-progreso` - Puntos totales, disponibles, streak
- `GET /ranking` - Top 10 usuarios por puntos

## 🔐 Seguridad

- Autenticación JWT
- Roles: ADMIN, VENDEDOR, CLIENTE
- CORS configurado para `http://localhost:5173` y `http://localhost:3000`

## 📦 Estructura del Proyecto

```
src/main/java/ClarityTimer/ClarityTimer_backend/
├── model/          # Entidades JPA
├── repository/     # Repositorios Spring Data
├── service/        # Lógica de negocio
├── controller/     # Controladores REST
├── dto/            # Data Transfer Objects
├── security/       # Configuración de seguridad JWT
├── config/         # Configuraciones (Swagger, etc.)
└── exception/      # Excepciones personalizadas
```

## 🗄️ Modelo de Datos

### Adaptación del Modelo E-commerce
```
E-commerce:  USUARIO → BOLETA → DETALLE_BOLETA → PRODUCTO → CATEGORIA
ClarityTimer: USUARIO → SESION → DETALLE_SESION → PERSONAJE → CATEGORIA_PERSONAJE
```

## 🧪 Pruebas

### Crear Usuario de Prueba
```bash
POST http://localhost:8080/api/v1/auth/register
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "nombre": "Test",
  "apellido": "User"
}
```

El usuario recibirá:
- ✅ 600 puntos de bienvenida
- ✅ Cinnamoroll gratis y activo
- ✅ Configuración por defecto

### Login
```bash
POST http://localhost:8080/api/v1/auth/login
{
  "username": "testuser",
  "password": "password123"
}
```

## 📝 Notas

- Los datos iniciales (categorías y personajes) se cargan automáticamente desde `data.sql`
- El personaje Cinnamoroll es el único con `es_default=true` y precio 0
- Los puntos se otorgan solo por pomodoros de tipo TRABAJO completados
- Los descansos no otorgan puntos

