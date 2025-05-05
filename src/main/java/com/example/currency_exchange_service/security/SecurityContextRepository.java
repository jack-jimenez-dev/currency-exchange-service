package com.example.currency_exchange_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Repositorio de contexto de seguridad para extraer y validar tokens JWT de las solicitudes.
 */
@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final AuthenticationManager authenticationManager;

    /**
     * Guarda el contexto de seguridad (no implementado).
     */
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        // No necesitamos guardar el contexto en este caso
        return Mono.empty();
    }

    /**
     * Carga el contexto de seguridad a partir del token JWT en la cabecera de autorización.
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring(7);
            Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
            return authenticationManager.authenticate(auth)
                    .map(SecurityContextImpl::new);
        }
        
        return Mono.empty();
    }
}
