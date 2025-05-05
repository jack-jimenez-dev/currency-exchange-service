package com.example.currency_exchange_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de autenticación.
 * Contiene el nombre de usuario y la contraseña.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @NotBlank(message = "El nombre de usuario es requerido")
    private String username;
    
    @NotBlank(message = "La contraseña es requerida")
    private String password;
}
