package com.example.currency_exchange_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Gestor de autenticación reactivo para validar tokens JWT.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;
    private final ReactiveUserDetailsService userDetailsService;

    /**
     * Autentica un token JWT.
     * 
     * @param authentication Autenticación con el token JWT
     * @return Autenticación validada
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = jwtUtil.extractUsername(authToken);
        
        return userDetailsService.findByUsername(username)
                .filter(userDetails -> jwtUtil.validateToken(authToken, userDetails))
                .map(userDetails -> {
                    return new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            null,
                            userDetails.getAuthorities()
                    );
                });
    }
}
