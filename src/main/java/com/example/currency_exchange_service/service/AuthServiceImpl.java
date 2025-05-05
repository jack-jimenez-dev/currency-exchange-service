package com.example.currency_exchange_service.service;

import com.example.currency_exchange_service.dto.AuthRequest;
import com.example.currency_exchange_service.dto.AuthResponse;
import com.example.currency_exchange_service.dto.RegisterRequest;
import com.example.currency_exchange_service.model.User;
import com.example.currency_exchange_service.repository.UserRepository;
import com.example.currency_exchange_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementación del servicio de autenticación.
 * Proporciona funcionalidad para autenticar usuarios y generar tokens JWT.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Autentica a un usuario y genera un token JWT.
     *
     * @param request Solicitud con nombre de usuario y contraseña
     * @return Respuesta con el token JWT generado
     */
    @Override
    public Mono<AuthResponse> authenticate(AuthRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .flatMap(user -> {
                    // Verificar la contraseña y manejar el resultado
                    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        // Contraseña correcta, generar token
                        String token = jwtUtil.generateToken(user);
                        return Mono.just(AuthResponse.builder()
                                .token(token)
                                .tokenType("Bearer")
                                .username(user.getUsername())
                                .role(user.getRole())
                                .build());
                    } else {
                        // Contraseña incorrecta
                        log.warn("Contraseña incorrecta para el usuario: {}", request.getUsername());
                        return Mono.error(new BadCredentialsException("Credenciales inválidas"));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // Usuario no encontrado
                    log.warn("Usuario no encontrado: {}", request.getUsername());
                    return Mono.error(new BadCredentialsException("Credenciales inválidas"));
                }));
    }

    @Override
    public Mono<User> registerUser(RegisterRequest request) {
        log.info("Registrando nuevo usuario: {}", request.getUsername());

        return userRepository.findByUsername(request.getUsername())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        // El usuario ya existe
                        log.warn("El usuario ya existe: {}", request.getUsername());
                        return Mono.error(new RuntimeException("El nombre de usuario ya está en uso"));
                    } else {
                        // Crear nuevo usuario
                        String role = (request.getRole() != null &&
                                      (request.getRole().equals("ADMIN") || request.getRole().equals("USER")))
                                      ? request.getRole() : "USER";

                        User newUser = User.builder()
                                .username(request.getUsername())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .email(request.getEmail())
                                .role(role)
                                .enabled(true)
                                .build();

                        return userRepository.save(newUser);
                    }
                });
    }
}
