package com.example.currency_exchange_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para solicitudes de conversión de moneda.
 * Contiene el monto a convertir, la moneda origen y la moneda destino.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeRequest {
    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal amount;

    @NotBlank(message = "La moneda origen es requerida")
    private String sourceCurrency;

    @NotBlank(message = "La moneda destino es requerida")
    private String targetCurrency;
}
