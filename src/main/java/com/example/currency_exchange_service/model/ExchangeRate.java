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
 * Entidad que representa una tasa de cambio entre dos monedas.
 * Incluye tasas para operaciones de compra y venta.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("exchange_rates")
public class ExchangeRate {
    @Id
    private Long id;
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;        // Tasa de cambio general (promedio)
    private BigDecimal buyRate;     // Tasa para comprar la moneda destino
    private BigDecimal sellRate;    // Tasa para vender la moneda destino
    private LocalDateTime lastUpdated;
}
