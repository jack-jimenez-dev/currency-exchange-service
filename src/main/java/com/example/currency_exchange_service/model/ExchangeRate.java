package com.example.currency_exchange_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private BigDecimal rate;
    private LocalDateTime lastUpdated;
}
