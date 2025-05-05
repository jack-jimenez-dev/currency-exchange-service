package com.example.currency_exchange_service.config;

import com.example.currency_exchange_service.security.AuthenticationManager;
import com.example.currency_exchange_service.security.SecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Configuración de seguridad para la aplicación.
 * Define reglas de acceso y configuración de autenticación.
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    /**
     * Configura la cadena de filtros de seguridad.
     *
     * @param http Configuración de seguridad HTTP
     * @return Cadena de filtros de seguridad configurada
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) ->
                    Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
                )
                .accessDeniedHandler((swe, e) ->
                    Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
                )
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/api/v1/auth/**").permitAll()
                .pathMatchers("/api/v1/test/**").permitAll() // Permitir acceso a endpoints de prueba
                .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                .pathMatchers("/h2-console/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/v1/exchange-rates/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/v1/currency-exchange/convert").permitAll()
                .pathMatchers("/api/v1/conversion-history/**").hasRole("ADMIN")
                .anyExchange().authenticated()
                .and()
                .build();
    }

    /**
     * Configura el codificador de contraseñas.
     *
     * @return Codificador de contraseñas BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
