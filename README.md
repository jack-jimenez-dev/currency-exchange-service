# Servicio de Conversión de Moneda

## Descripción General
Este microservicio reactivo basado en Spring Boot proporciona una API para la conversión de monedas. Permite a los usuarios convertir cantidades entre diferentes divisas utilizando tasas de cambio actualizadas, con soporte para tasas de compra y venta.

## Características Principales
- API RESTful reactiva para conversión de monedas
- Almacenamiento en caché de resultados durante 5 minutos para mejorar el rendimiento
- Historial de conversiones realizadas
- Consulta de tasas de cambio para compra y venta
- Seguridad mediante JWT (JSON Web Tokens)
- Patrones de resiliencia: Circuit Breaker, Retry y Fallback
- Base de datos H2 en memoria para almacenar tasas de cambio
- Dockerizado para facilitar la implementación
- Documentación de API con Swagger

## Arquitectura

### Componentes Principales

#### Controladores
- `CurrencyExchangeController`: Maneja las solicitudes HTTP para la conversión de monedas.
- `ExchangeRateController`: Maneja las solicitudes HTTP para consultar tasas de cambio.
- `ConversionHistoryController`: Maneja las solicitudes HTTP para consultar el historial de conversiones.
- `AuthController`: Maneja las solicitudes HTTP para autenticación y generación de tokens JWT.

#### Servicios
- `CurrencyExchangeService`: Define la interfaz para las operaciones de conversión de moneda.
- `CurrencyExchangeServiceImpl`: Implementa la lógica de negocio para la conversión de monedas, incluyendo la validación de monedas, obtención de tasas de cambio y cálculos de conversión. Implementa un mecanismo de caché manual para almacenar resultados durante 5 minutos.
- `AuthService`: Define la interfaz para las operaciones de autenticación.
- `AuthServiceImpl`: Implementa la lógica de autenticación y generación de tokens JWT.

#### Repositorios
- `CurrencyRepository`: Gestiona las operaciones de base de datos para las entidades de moneda.
- `ExchangeRateRepository`: Gestiona las operaciones de base de datos para las entidades de tasas de cambio.
- `ConversionHistoryRepository`: Gestiona las operaciones de base de datos para el historial de conversiones.
- `UserRepository`: Gestiona las operaciones de base de datos para los usuarios.

#### Modelos
- `Currency`: Representa una moneda con código y nombre.
- `ExchangeRate`: Representa una tasa de cambio entre dos monedas, incluyendo tasas de compra y venta.
- `ConversionHistory`: Representa una conversión realizada en el sistema.
- `User`: Representa un usuario del sistema con roles y permisos.

#### DTOs
- `CurrencyExchangeRequest`: Contiene los datos de solicitud para una conversión de moneda.
- `CurrencyExchangeResponse`: Contiene los datos de respuesta para una conversión de moneda.
- `ExchangeRateResponse`: Contiene los datos de respuesta para una consulta de tasa de cambio.
- `ConversionHistoryResponse`: Contiene los datos de respuesta para una consulta de historial.
- `AuthRequest`: Contiene los datos de solicitud para autenticación.
- `AuthResponse`: Contiene los datos de respuesta para autenticación, incluyendo el token JWT.

#### Seguridad
- `JwtUtil`: Utilidad para generar y validar tokens JWT.
- `AuthenticationManager`: Gestiona la autenticación con tokens JWT.
- `SecurityContextRepository`: Extrae tokens JWT de las solicitudes.
- `UserDetailsServiceImpl`: Carga detalles de usuario para autenticación.
- `SecurityConfig`: Configura reglas de seguridad y acceso.

#### Resiliencia
- `ResilienceConfig`: Configura patrones de resiliencia como Circuit Breaker, Retry y Fallback.

#### Manejo de Excepciones
- `GlobalExceptionHandler`: Maneja excepciones a nivel global y proporciona respuestas de error consistentes.
- Excepciones personalizadas como `CurrencyNotFoundException`, `ExchangeRateNotFoundException` y otras.

### Flujo de Datos para Conversión de Moneda
1. El cliente envía una solicitud POST a `/api/v1/currency-exchange/convert` con los detalles de la conversión.
2. El controlador recibe la solicitud y la pasa al servicio.
3. El servicio verifica si el resultado está en caché. Si es así, devuelve el resultado almacenado.
4. Si no está en caché, el servicio valida las monedas, obtiene la tasa de cambio y calcula el monto convertido.
5. El resultado se almacena en caché durante 5 minutos.
6. La conversión se registra en el historial de conversiones.
7. El resultado se devuelve al cliente.

### Flujo de Datos para Registro y Autenticación
1. **Registro de Usuario**:
   - El cliente envía una solicitud POST a `/api/v1/auth/register` con los datos del nuevo usuario.
   - El controlador recibe la solicitud y la pasa al servicio de autenticación.
   - El servicio verifica que el nombre de usuario no exista ya en la base de datos.
   - Si el nombre de usuario está disponible, se crea un nuevo usuario con la contraseña encriptada.
   - El usuario creado se devuelve al cliente.

2. **Autenticación**:
   - El cliente envía una solicitud POST a `/api/v1/auth/login` con sus credenciales.
   - El controlador recibe la solicitud y la pasa al servicio de autenticación.
   - El servicio verifica las credenciales contra la base de datos.
   - Si las credenciales son válidas, se genera un token JWT.
   - El token JWT se devuelve al cliente.

### Flujo de Datos para Consulta de Historial
1. El cliente envía una solicitud GET a `/api/v1/conversion-history` con parámetros de filtrado opcionales.
2. El sistema verifica que el cliente esté autenticado y tenga el rol ADMIN.
3. El controlador recibe la solicitud y la pasa al servicio.
4. El servicio consulta la base de datos aplicando los filtros proporcionados.
5. Los resultados se transforman en DTOs y se devuelven al cliente.

## Implementación de Caché

El servicio implementa un mecanismo de caché manual utilizando `ConcurrentHashMap` para almacenar los resultados de conversión durante 5 minutos. Esto mejora el rendimiento al evitar consultas repetidas a la base de datos para las mismas conversiones.

```java
// Caché manual usando ConcurrentHashMap
private final Map<String, CachedResponse> cache = new ConcurrentHashMap<>();

// Clase para almacenar respuestas en caché con timestamp
private static class CachedResponse {
    private final CurrencyExchangeResponse response;
    private final LocalDateTime timestamp;

    public CachedResponse(CurrencyExchangeResponse response) {
        this.response = response;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isExpired() {
        // Expirar después de 5 minutos (300 segundos)
        return Duration.between(timestamp, LocalDateTime.now()).getSeconds() > 300;
    }
}
```

## Configuración y Ejecución

### Requisitos Previos
- Java 17 o superior
- Docker (para ejecutar en contenedor)

### Ejecución Local
1. Clonar el repositorio
2. Navegar al directorio del proyecto
3. Ejecutar `./mvnw spring-boot:run`
4. La aplicación estará disponible en `http://localhost:8080`

### Ejecución con Docker
1. Construir la imagen Docker:
   ```
   docker build -t currency-exchange-service .
   ```
2. Ejecutar el contenedor:
   ```
   docker run -p 8080:8080 currency-exchange-service
   ```
3. La aplicación estará disponible en `http://localhost:8080`

## API Endpoints

### 1. Convertir Moneda
- **URL**: `/api/v1/currency-exchange/convert`
- **Método**: POST
- **Descripción**: Convierte un monto de una moneda a otra utilizando la tasa de cambio actual.
- **Autenticación**: No requerida
- **Cuerpo de la Solicitud**:
  ```json
  {
    "amount": 100,
    "sourceCurrency": "USD",
    "targetCurrency": "EUR"
  }
  ```
- **Respuesta Exitosa** (200 OK):
  ```json
  {
    "amount": 100,
    "convertedAmount": 93.00,
    "sourceCurrency": "USD",
    "targetCurrency": "EUR",
    "exchangeRate": 0.93
  }
  ```
- **Respuesta de Error** (404 Not Found):
  ```json
  {
    "message": "Moneda origen no encontrada: XYZ",
    "status": 404,
    "timestamp": "2023-11-15T14:30:45"
  }
  ```
- **Ejemplo de Uso con cURL**:
  ```bash
  curl -X POST http://localhost:8080/api/v1/currency-exchange/convert \
    -H "Content-Type: application/json" \
    -d '{"amount": 100, "sourceCurrency": "USD", "targetCurrency": "EUR"}'
  ```

### 2. Consultar Tasa de Cambio
- **URL**: `/api/v1/exchange-rates/{sourceCurrency}/{targetCurrency}`
- **Método**: GET
- **Descripción**: Obtiene información detallada sobre la tasa de cambio entre dos monedas, incluyendo tasas de compra y venta.
- **Autenticación**: No requerida
- **Parámetros de Ruta**:
  - `sourceCurrency`: Código de la moneda de origen (ej. "USD")
  - `targetCurrency`: Código de la moneda de destino (ej. "EUR")
- **Respuesta Exitosa** (200 OK):
  ```json
  {
    "sourceCurrency": "USD",
    "targetCurrency": "EUR",
    "rate": 0.93,
    "buyRate": 0.92,
    "sellRate": 0.94,
    "lastUpdated": "2023-11-15T10:30:00"
  }
  ```
- **Respuesta de Error** (404 Not Found):
  ```json
  {
    "message": "Tasa de cambio no encontrada para USD a XYZ",
    "status": 404,
    "timestamp": "2023-11-15T14:30:45"
  }
  ```
- **Ejemplo de Uso con cURL**:
  ```bash
  curl -X GET http://localhost:8080/api/v1/exchange-rates/USD/EUR
  ```

### 3. Consultar Historial de Conversiones
- **URL**: `/api/v1/conversion-history`
- **Método**: GET
- **Descripción**: Obtiene el historial de conversiones realizadas, con opciones de filtrado.
- **Autenticación**: Requerida (JWT Token con rol ADMIN)
- **Parámetros de Consulta** (todos opcionales):
  - `sourceCurrency`: Filtrar por moneda de origen (ej. "USD")
  - `targetCurrency`: Filtrar por moneda de destino (ej. "EUR")
  - `startDate`: Fecha de inicio para filtrar (formato ISO: "2023-11-01T00:00:00")
  - `endDate`: Fecha de fin para filtrar (formato ISO: "2023-11-30T23:59:59")
- **Respuesta Exitosa** (200 OK):
  ```json
  [
    {
      "id": 1,
      "sourceCurrency": "USD",
      "targetCurrency": "EUR",
      "originalAmount": 100.00,
      "convertedAmount": 93.00,
      "exchangeRate": 0.93,
      "conversionDate": "2023-11-15T14:30:45"
    },
    {
      "id": 2,
      "sourceCurrency": "USD",
      "targetCurrency": "EUR",
      "originalAmount": 200.00,
      "convertedAmount": 186.00,
      "exchangeRate": 0.93,
      "conversionDate": "2023-11-16T09:15:22"
    }
  ]
  ```
- **Respuesta de Error** (401 Unauthorized):
  ```json
  {
    "message": "No autorizado",
    "status": 401,
    "timestamp": "2023-11-15T14:30:45"
  }
  ```
- **Ejemplo de Uso con cURL**:
  ```bash
  curl -X GET "http://localhost:8080/api/v1/conversion-history?sourceCurrency=USD&targetCurrency=EUR" \
    -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  ```

### 4. Registro de Usuario
- **URL**: `/api/v1/auth/register`
- **Método**: POST
- **Descripción**: Registra un nuevo usuario en el sistema.
- **Autenticación**: No requerida
- **Cuerpo de la Solicitud**:
  ```json
  {
    "username": "nuevoUsuario",
    "password": "miContraseña123",
    "email": "usuario@ejemplo.com",
    "role": "ADMIN"  // Opcional, valores posibles: "ADMIN" o "USER". Por defecto es "USER"
  }
  ```
- **Respuesta Exitosa** (200 OK):
  ```json
  {
    "id": 1,
    "username": "nuevoUsuario",
    "email": "usuario@ejemplo.com",
    "role": "ADMIN",
    "enabled": true
  }
  ```
- **Respuesta de Error** (400 Bad Request):
  ```json
  {
    "message": "El nombre de usuario ya está en uso",
    "status": 400,
    "timestamp": "2023-11-15T14:30:45"
  }
  ```
- **Ejemplo de Uso con cURL**:
  ```bash
  curl -X POST http://localhost:8080/api/v1/auth/register \
    -H "Content-Type: application/json" \
    -d '{"username": "nuevoUsuario", "password": "miContraseña123", "email": "usuario@ejemplo.com", "role": "ADMIN"}'
  ```

### 5. Autenticación
- **URL**: `/api/v1/auth/login`
- **Método**: POST
- **Descripción**: Autentica a un usuario y genera un token JWT.
- **Autenticación**: No requerida
- **Cuerpo de la Solicitud**:
  ```json
  {
    "username": "nuevoUsuario",
    "password": "miContraseña123"
  }
  ```
- **Respuesta Exitosa** (200 OK):
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "username": "nuevoUsuario",
    "role": "ADMIN"
  }
  ```
- **Respuesta de Error** (401 Unauthorized):
  ```json
  {
    "message": "Credenciales inválidas",
    "status": 401,
    "timestamp": "2023-11-15T14:30:45"
  }
  ```
- **Ejemplo de Uso con cURL**:
  ```bash
  curl -X POST http://localhost:8080/api/v1/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username": "nuevoUsuario", "password": "miContraseña123"}'
  ```

## Documentación de la API
La documentación de la API está disponible a través de Swagger UI en `http://localhost:8080/swagger-ui.html` cuando la aplicación está en ejecución.

## Base de Datos
La aplicación utiliza una base de datos H2 en memoria con las siguientes tablas:
- `currencies`: Almacena información sobre las monedas disponibles.
- `exchange_rates`: Almacena las tasas de cambio entre pares de monedas, incluyendo tasas de compra y venta.
- `conversion_history`: Almacena el historial de todas las conversiones realizadas.
- `users`: Almacena información de usuarios para autenticación y autorización.

Los datos iniciales se cargan desde los archivos `schema.sql` y `data.sql` durante el inicio de la aplicación.

### Estructura de Tablas

#### Tabla `currencies`
```sql
CREATE TABLE currencies (
    id IDENTITY PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL
);
```

#### Tabla `exchange_rates`
```sql
CREATE TABLE exchange_rates (
    id IDENTITY PRIMARY KEY,
    source_currency_code VARCHAR(3) NOT NULL,
    target_currency_code VARCHAR(3) NOT NULL,
    rate DECIMAL(19, 6) NOT NULL,
    buy_rate DECIMAL(19, 6),
    sell_rate DECIMAL(19, 6),
    last_updated TIMESTAMP NOT NULL,
    CONSTRAINT unique_currency_pair UNIQUE (source_currency_code, target_currency_code)
);
```

#### Tabla `conversion_history`
```sql
CREATE TABLE conversion_history (
    id IDENTITY PRIMARY KEY,
    source_currency_code VARCHAR(3) NOT NULL,
    target_currency_code VARCHAR(3) NOT NULL,
    original_amount DECIMAL(19, 6) NOT NULL,
    converted_amount DECIMAL(19, 6) NOT NULL,
    exchange_rate DECIMAL(19, 6) NOT NULL,
    conversion_date TIMESTAMP NOT NULL,
    ip_address VARCHAR(45),
    user_id VARCHAR(50)
);
```

#### Tabla `users`
```sql
CREATE TABLE users (
    id IDENTITY PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL
);
```

## Pruebas
Para ejecutar las pruebas, utilice el siguiente comando:
```
./mvnw test
```

## Seguridad

La aplicación implementa seguridad mediante JWT (JSON Web Tokens) para proteger ciertos endpoints. El flujo completo es el siguiente:

1. **Registro**: El usuario se registra primero mediante el endpoint `/api/v1/auth/register` proporcionando un nombre de usuario, contraseña, correo electrónico y opcionalmente un rol.
2. **Autenticación**: El usuario envía sus credenciales al endpoint `/api/v1/auth/login`.
3. **Generación de Token**: Si las credenciales son válidas, el servidor genera un token JWT y lo devuelve al cliente.
4. **Uso del Token**: El cliente debe incluir este token en el encabezado `Authorization` de las solicitudes posteriores.
5. **Validación**: El servidor valida el token y permite o deniega el acceso según los roles del usuario.

### Configuración de Seguridad

Los siguientes endpoints están disponibles sin autenticación:
- `/api/v1/auth/**`: Endpoints de autenticación
- `/api/v1/currency-exchange/convert`: Conversión de monedas
- `/api/v1/exchange-rates/**`: Consulta de tasas de cambio
- `/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`: Documentación de la API
- `/h2-console/**`: Consola de la base de datos H2

Los siguientes endpoints requieren autenticación:
- `/api/v1/conversion-history/**`: Requiere rol ADMIN

## Patrones de Resiliencia

La aplicación implementa los siguientes patrones de resiliencia utilizando Resilience4j:

### Circuit Breaker
Evita que la aplicación intente operaciones que probablemente fallarán, permitiendo una degradación elegante del servicio.

Configuración:
- Tamaño de ventana deslizante: 10 llamadas
- Umbral de tasa de fallos: 50%
- Tiempo de espera en estado abierto: 10 segundos

### Retry
Reintenta automáticamente operaciones fallidas con una estrategia de retardo exponencial.

Configuración:
- Número máximo de intentos: 3
- Duración de espera inicial: 500 ms
- Excepciones a reintentar: Todas las excepciones

### Fallback
Proporciona una respuesta alternativa cuando una operación falla después de agotar los reintentos.

## Solución de Problemas

### Problemas Comunes
1. **Error de conexión a la base de datos**: Verifique que la configuración de la base de datos en `application.properties` sea correcta.
2. **Error al iniciar la aplicación**: Asegúrese de que el puerto 8080 no esté siendo utilizado por otra aplicación.
3. **Error en la conversión de moneda**: Verifique que las monedas especificadas existan en la base de datos y que haya una tasa de cambio definida entre ellas.
4. **Error de registro de usuario**: Asegúrese de que el nombre de usuario no esté ya en uso y que la contraseña cumpla con los requisitos mínimos (al menos 6 caracteres).
5. **Error de autenticación**: Asegúrese de que el token JWT sea válido y no haya expirado. Verifique que esté utilizando el formato correcto en el encabezado de autorización: `Authorization: Bearer {token}`.
6. **Error de autorización**: Verifique que el usuario tenga los roles necesarios para acceder al recurso solicitado. Para acceder al historial de conversiones, el usuario debe tener el rol ADMIN.
