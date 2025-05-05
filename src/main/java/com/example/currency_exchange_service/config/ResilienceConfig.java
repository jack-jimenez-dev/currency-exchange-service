package com.example.currency_exchange_service.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuración de patrones de resiliencia para la aplicación.
 * Implementa Circuit Breaker, Retry y Fallback utilizando Resilience4j.
 */
@Configuration
public class ResilienceConfig {

    /**
     * Configura el Circuit Breaker por defecto con los siguientes parámetros:
     * - Tamaño de ventana deslizante: 10 llamadas
     * - Umbral de tasa de fallos: 50%
     * - Tiempo de espera en estado abierto: 10 segundos
     * - Llamadas permitidas en estado semi-abierto: 5
     * - Umbral de tasa de llamadas lentas: 50%
     * - Umbral de duración para llamadas lentas: 2 segundos
     * - Tiempo límite para operaciones: 3 segundos
     * - Reintentos: 3 con retardo exponencial
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(10)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        .permittedNumberOfCallsInHalfOpenState(5)
                        .slowCallRateThreshold(50)
                        .slowCallDurationThreshold(Duration.ofSeconds(2))
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(3))
                        .build())
                .build());
    }

    /**
     * Configura un Circuit Breaker específico para operaciones de conversión de moneda
     * con parámetros más estrictos.
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> currencyExchangeCustomizer() {
        return factory -> factory.configure(builder -> builder
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(5)
                        .failureRateThreshold(40)
                        .waitDurationInOpenState(Duration.ofSeconds(5))
                        .permittedNumberOfCallsInHalfOpenState(3)
                        .slowCallRateThreshold(40)
                        .slowCallDurationThreshold(Duration.ofSeconds(1))
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(2))
                        .build()), "currencyExchange");
    }

    /**
     * Configura el patrón de Retry para reintentar operaciones fallidas.
     */
    @Bean
    public RetryRegistry retryRegistry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .retryExceptions(Exception.class)
                .build();

        return RetryRegistry.of(config);
    }

    /**
     * Personaliza la configuración de Retry para operaciones de conversión de moneda.
     */
    @Bean
    public RetryConfigCustomizer currencyExchangeRetryConfig() {
        return RetryConfigCustomizer.of("currencyExchange", builder -> builder
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .retryExceptions(Exception.class));
    }
}
