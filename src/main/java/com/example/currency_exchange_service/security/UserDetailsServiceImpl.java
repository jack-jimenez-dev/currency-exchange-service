package com.example.currency_exchange_service.security;

import com.example.currency_exchange_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementación del servicio de detalles de usuario reactivo.
 * Carga los detalles de usuario desde la base de datos.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    /**
     * Carga los detalles de usuario por nombre de usuario.
     * 
     * @param username Nombre de usuario
     * @return Detalles del usuario
     */
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .cast(UserDetails.class)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Usuario no encontrado: " + username)));
    }
}
