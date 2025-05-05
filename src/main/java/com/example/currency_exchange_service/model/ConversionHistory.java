package com.example.currency_exchange_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa el histórico de conversiones de moneda.
 * Almacena información sobre cada conversión realizada en el sistema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("conversion_history")
public class ConversionHistory {
    @Id
    private Long id;
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal originalAmount;
    private BigDecimal convertedAmount;
    private BigDecimal exchangeRate;
    private LocalDateTime conversionDate;
    private String ipAddress; // Dirección IP del cliente que realizó la conversión
    private String userId; // ID del usuario que realizó la conversión (si está autenticado)
}
