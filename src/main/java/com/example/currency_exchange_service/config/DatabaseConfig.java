package com.example.currency_exchange_service.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

/**
 * Configuración de la base de datos para la aplicación.
 * Inicializa la base de datos con el esquema y datos iniciales.
 */
@Configuration
@Slf4j
public class DatabaseConfig {

    /**
     * Inicializa la base de datos con scripts SQL.
     * - schema.sql: Define la estructura de las tablas
     * - data.sql: Carga datos iniciales de monedas y tasas de cambio
     */
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        log.info("Inicializando base de datos...");
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.addScript(new ClassPathResource("data.sql"));

        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
