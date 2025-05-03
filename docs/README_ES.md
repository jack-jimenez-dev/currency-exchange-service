# Servicio de Conversión de Moneda

## Descripción General
Este microservicio reactivo basado en Spring Boot proporciona una API para la conversión de monedas. Permite a los usuarios convertir cantidades entre diferentes divisas utilizando tasas de cambio actualizadas.

## Características Principales
- API RESTful reactiva para conversión de monedas
- Almacenamiento en caché de resultados durante 5 minutos para mejorar el rendimiento
- Base de datos H2 en memoria para almacenar tasas de cambio
- Dockerizado para facilitar la implementación
- Documentación de API con Swagger

## Arquitectura

### Componentes Principales

#### Controladores
- `CurrencyExchangeController`: Maneja las solicitudes HTTP para la conversión de monedas.

#### Servicios
- `CurrencyExchangeService`: Define la interfaz para las operaciones de conversión de moneda.
- `CurrencyExchangeServiceImpl`: Implementa la lógica de negocio para la conversión de monedas, incluyendo la validación de monedas, obtención de tasas de cambio y cálculos de conversión. Implementa un mecanismo de caché manual para almacenar resultados durante 5 minutos.

#### Repositorios
- `CurrencyRepository`: Gestiona las operaciones de base de datos para las entidades de moneda.
- `ExchangeRateRepository`: Gestiona las operaciones de base de datos para las entidades de tasas de cambio.

#### Modelos
- `Currency`: Representa una moneda con código y nombre.
- `ExchangeRate`: Representa una tasa de cambio entre dos monedas.

#### DTOs
- `CurrencyExchangeRequest`: Contiene los datos de solicitud para una conversión de moneda.
- `CurrencyExchangeResponse`: Contiene los datos de respuesta para una conversión de moneda.

#### Manejo de Excepciones
- `GlobalExceptionHandler`: Maneja excepciones a nivel global y proporciona respuestas de error consistentes.
- Excepciones personalizadas como `CurrencyNotFoundException` y `ExchangeRateNotFoundException`.

### Flujo de Datos
1. El cliente envía una solicitud POST a `/api/v1/currency-exchange/convert` con los detalles de la conversión.
2. El controlador recibe la solicitud y la pasa al servicio.
3. El servicio verifica si el resultado está en caché. Si es así, devuelve el resultado almacenado.
4. Si no está en caché, el servicio valida las monedas, obtiene la tasa de cambio y calcula el monto convertido.
5. El resultado se almacena en caché durante 5 minutos y se devuelve al cliente.

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

### Convertir Moneda
- **URL**: `/api/v1/currency-exchange/convert`
- **Método**: POST
- **Cuerpo de la Solicitud**:
  ```json
  {
    "amount": 100,
    "sourceCurrency": "USD",
    "targetCurrency": "EUR"
  }
  ```
- **Respuesta Exitosa**:
  ```json
  {
    "amount": 100,
    "convertedAmount": 93.00,
    "sourceCurrency": "USD",
    "targetCurrency": "EUR",
    "exchangeRate": 0.93
  }
  ```

## Documentación de la API
La documentación de la API está disponible a través de Swagger UI en `http://localhost:8080/swagger-ui.html` cuando la aplicación está en ejecución.

## Base de Datos
La aplicación utiliza una base de datos H2 en memoria con las siguientes tablas:
- `currencies`: Almacena información sobre las monedas disponibles.
- `exchange_rates`: Almacena las tasas de cambio entre pares de monedas.

Los datos iniciales se cargan desde los archivos `schema.sql` y `data.sql` durante el inicio de la aplicación.

## Pruebas
Para ejecutar las pruebas, utilice el siguiente comando:
```
./mvnw test
```

## Solución de Problemas

### Problemas Comunes
1. **Error de conexión a la base de datos**: Verifique que la configuración de la base de datos en `application.properties` sea correcta.
2. **Error al iniciar la aplicación**: Asegúrese de que el puerto 8080 no esté siendo utilizado por otra aplicación.
3. **Error en la conversión de moneda**: Verifique que las monedas especificadas existan en la base de datos y que haya una tasa de cambio definida entre ellas.
