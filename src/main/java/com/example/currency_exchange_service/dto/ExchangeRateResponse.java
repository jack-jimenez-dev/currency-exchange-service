package com.example.currency_exchange_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para respuestas de consulta de tasas de cambio.
 * Incluye información sobre las tasas de compra, venta y promedio.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {
    private String sourceCurrency;        // Moneda origen
    private String targetCurrency;        // Moneda destino
    private BigDecimal rate;              // Tasa de cambio promedio
    private BigDecimal buyRate;           // Tasa para comprar la moneda destino
    private BigDecimal sellRate;          // Tasa para vender la moneda destino
    private LocalDateTime lastUpdated;    // Última actualización de la tasa
}
