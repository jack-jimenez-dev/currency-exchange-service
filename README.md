# Microservicio de Conversión de Monedas

Este proyecto implementa un microservicio reactivo para la conversión de monedas utilizando Spring Boot, WebFlux y Java 17.

[Documentación en Español](docs/README_ES.md)

## Arquitectura

El microservicio sigue una arquitectura en capas con los siguientes componentes:

### Capa de Dominio
- **Entidades**: `Currency` y `ExchangeRate` representan las monedas y tasas de cambio.
- **DTOs**: `CurrencyExchangeRequest` y `CurrencyExchangeResponse` para la comunicación con el cliente.

### Capa de Repositorio
- Interfaces reactivas que extienden `ReactiveCrudRepository` para acceso a datos.
- Utiliza R2DBC para operaciones de base de datos reactivas.

### Capa de Servicio
- Implementa la lógica de negocio para la conversión de monedas.
- Utiliza caché manual para optimizar las operaciones (ventana de 5 minutos).
- Implementa validación de monedas y manejo de errores.

### Capa de Controlador
- Expone endpoints REST reactivos para la conversión de monedas.
- Maneja validaciones y errores.

### Características Técnicas
- **Programación Reactiva**: Utiliza WebFlux y Project Reactor.
- **Caché**: Implementación manual con ConcurrentHashMap para almacenar resultados por 5 minutos.
- **Documentación API**: OpenAPI/Swagger.
- **Base de Datos**: H2 en memoria con R2DBC.
- **Dockerización**: Imagen basada en Eclipse Temurin JRE 17.

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior
- Docker (opcional, para ejecutar en contenedor)

## Cómo Ejecutar

### Ejecutar localmente

1. Clonar el repositorio:
   ```bash
   git clone <url-del-repositorio>
   cd currency-exchange-service
   ```

2. Compilar el proyecto:
   ```bash
   ./mvnw clean package
   ```

3. Ejecutar la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

4. La aplicación estará disponible en: http://localhost:8080

### Ejecutar con Docker

1. Construir la imagen Docker:
   ```bash
   docker build -t currency-exchange-service .
   ```

2. Ejecutar el contenedor:
   ```bash
   docker run -p 8080:8080 currency-exchange-service
   ```

## Endpoints API

### Convertir Moneda
- **URL**: `/api/v1/currency-exchange/convert`
- **Método**: POST
- **Cuerpo de la solicitud**:
  ```json
  {
    "amount": 100,
    "sourceCurrency": "USD",
    "targetCurrency": "EUR"
  }
  ```
- **Respuesta exitosa**:
  ```json
  {
    "amount": 100,
    "convertedAmount": 93.00,
    "sourceCurrency": "USD",
    "targetCurrency": "EUR",
    "exchangeRate": 0.93
  }
  ```

## Documentación API

La documentación de la API está disponible a través de Swagger UI:
- URL: http://localhost:8080/swagger-ui.html

## Características Implementadas

1. **Conversión de Monedas**: Permite convertir entre diferentes monedas.
2. **Caché Manual**: Optimiza las operaciones almacenando resultados por 5 minutos usando ConcurrentHashMap.
3. **Validación**: Valida los datos de entrada.
4. **Manejo de Errores**: Implementa manejo global de excepciones.
5. **Dockerización**: Permite ejecutar en contenedores.
6. **Pruebas Unitarias**: Cobertura de pruebas para servicios y controladores.

## Estructura del Proyecto

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── currency_exchange_service
│   │               ├── config
│   │               │   ├── CacheConfig.java (simplificado)
│   │               │   ├── DatabaseConfig.java
│   │               │   └── OpenApiConfig.java
│   │               ├── controller
│   │               │   └── CurrencyExchangeController.java
│   │               ├── dto
│   │               │   ├── CurrencyExchangeRequest.java
│   │               │   └── CurrencyExchangeResponse.java
│   │               ├── exception
│   │               │   ├── CurrencyNotFoundException.java
│   │               │   ├── ErrorResponse.java
│   │               │   ├── ExchangeRateNotFoundException.java
│   │               │   └── GlobalExceptionHandler.java
│   │               ├── model
│   │               │   ├── Currency.java
│   │               │   └── ExchangeRate.java
│   │               ├── repository
│   │               │   ├── CurrencyRepository.java
│   │               │   └── ExchangeRateRepository.java
│   │               ├── service
│   │               │   ├── CurrencyExchangeService.java
│   │               │   └── CurrencyExchangeServiceImpl.java (con caché manual)
│   │               └── CurrencyExchangeServiceApplication.java
│   └── resources
│       ├── application.properties
│       ├── data.sql
│       └── schema.sql
└── test
    └── java
        └── com
            └── example
                └── currency_exchange_service
                    ├── controller
                    │   └── CurrencyExchangeControllerTest.java
                    ├── service
                    │   └── CurrencyExchangeServiceImplTest.java
                    └── CurrencyExchangeServiceApplicationTests.java
```

## Decisiones de Diseño

1. **Programación Reactiva**: Se eligió WebFlux para implementar programación reactiva, permitiendo manejar más solicitudes con menos recursos.

2. **Caché Manual**: Se implementó una caché manual con ConcurrentHashMap para almacenar resultados por 5 minutos, evitando problemas de compatibilidad con WebFlux.

3. **Base de Datos H2**: Para desarrollo y pruebas, se utilizó H2 en memoria con soporte R2DBC para operaciones reactivas.

4. **Dockerización**: Se creó un Dockerfile multi-etapa para optimizar el tamaño de la imagen final.

## Posibles Mejoras

1. Implementar autenticación y autorización.
2. Agregar más monedas y tasas de cambio.
3. Implementar actualización automática de tasas de cambio desde APIs externas.
4. Mejorar la cobertura de pruebas.
5. Implementar métricas y monitoreo.
