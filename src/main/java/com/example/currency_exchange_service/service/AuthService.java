package com.example.currency_exchange_service.service;

import com.example.currency_exchange_service.dto.AuthRequest;
import com.example.currency_exchange_service.dto.AuthResponse;
import com.example.currency_exchange_service.dto.RegisterRequest;
import com.example.currency_exchange_service.model.User;
import reactor.core.publisher.Mono;

/**
 * Interfaz para el servicio de autenticación.
 * Define operaciones para autenticar usuarios y generar tokens JWT.
 */
public interface AuthService {

    /**
     * Autentica a un usuario y genera un token JWT.
     *
     * @param request Solicitud con nombre de usuario y contraseña
     * @return Respuesta con el token JWT generado
     */
    Mono<AuthResponse> authenticate(AuthRequest request);

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request Solicitud con datos del nuevo usuario
     * @return Usuario creado
     */
    Mono<User> registerUser(RegisterRequest request);
}
