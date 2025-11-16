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

### Requisitos
- Java 17+
- Maven 3.6+
- MySQL 8.0+

### Configuración

1. **Configurar base de datos MySQL:**
   ```sql
   CREATE DATABASE claritytimer_db;
   ```

2. **Configurar `application.properties`:**
   - Ajustar `spring.datasource.username` y `spring.datasource.password`
   - La base de datos se crea automáticamente si no existe

3. **Ejecutar la aplicación:**
   ```bash
   mvn spring-boot:run
   ```

4. **Acceder a Swagger:**
   - URL: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/api-docs

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

