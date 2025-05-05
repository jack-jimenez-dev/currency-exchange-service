package com.example.currency_exchange_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para respuestas de consulta del historial de conversiones.
 * Contiene información sobre una conversión realizada.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionHistoryResponse {
    private Long id;
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal originalAmount;
    private BigDecimal convertedAmount;
    private BigDecimal exchangeRate;
    private LocalDateTime conversionDate;
}
