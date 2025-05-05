package com.example.currency_exchange_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para solicitudes de consulta del historial de conversiones.
 * Permite filtrar por moneda origen, moneda destino y rango de fechas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionHistoryRequest {
    private String sourceCurrency;        // Filtro por moneda origen (opcional)
    private String targetCurrency;        // Filtro por moneda destino (opcional)
    private LocalDateTime startDate;      // Fecha de inicio para el filtro (opcional)
    private LocalDateTime endDate;        // Fecha de fin para el filtro (opcional)
    private Integer page;                 // Número de página para paginación
    private Integer size;                 // Tamaño de página para paginación
}
