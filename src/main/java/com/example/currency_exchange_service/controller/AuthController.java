package com.example.currency_exchange_service.controller;

import com.example.currency_exchange_service.dto.AuthRequest;
import com.example.currency_exchange_service.dto.AuthResponse;
import com.example.currency_exchange_service.dto.RegisterRequest;
import com.example.currency_exchange_service.model.User;
import com.example.currency_exchange_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controlador para operaciones de autenticación.
 * Expone endpoints REST para autenticar usuarios y generar tokens JWT.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para autenticar a un usuario y generar un token JWT.
     *
     * @param request Solicitud con nombre de usuario y contraseña
     * @return Respuesta con el token JWT generado
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        log.info("Solicitud de autenticación recibida para el usuario: {}", request.getUsername());
        return authService.authenticate(request)
                .map(ResponseEntity::ok);
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param request Solicitud con datos del nuevo usuario
     * @return Usuario creado
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<User>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Solicitud de registro recibida para el usuario: {}", request.getUsername());
        return authService.registerUser(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error al registrar usuario: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }


}
