package com.example.currency_exchange_service.repository;

import com.example.currency_exchange_service.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repositorio para acceder a los datos de usuarios.
 */
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    
    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username Nombre de usuario
     * @return Usuario encontrado o Mono vacío si no existe
     */
    Mono<User> findByUsername(String username);
}
