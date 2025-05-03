package com.example.currency_exchange_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para respuestas de conversión de moneda.
 * Contiene el monto original, el monto convertido, las monedas origen y destino,
 * y la tasa de cambio aplicada.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeResponse {
    private BigDecimal amount;            // Monto original
    private BigDecimal convertedAmount;   // Monto convertido
    private String sourceCurrency;        // Moneda origen
    private String targetCurrency;        // Moneda destino
    private BigDecimal exchangeRate;      // Tasa de cambio aplicada
}
