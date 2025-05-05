package com.example.currency_exchange_service.service;

import com.example.currency_exchange_service.dto.ConversionHistoryResponse;
import com.example.currency_exchange_service.dto.CurrencyExchangeRequest;
import com.example.currency_exchange_service.dto.CurrencyExchangeResponse;
import com.example.currency_exchange_service.dto.ExchangeRateResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Interfaz para el servicio de conversión de monedas.
 * Define operaciones para convertir monedas, consultar tasas de cambio y obtener historial.
 */
public interface CurrencyExchangeService {

    /**
     * Convierte un monto de una moneda a otra.
     *
     * @param request Solicitud con monto, moneda origen y moneda destino
     * @return Respuesta con el monto convertido y tasa de cambio aplicada
     */
    Mono<CurrencyExchangeResponse> convertCurrency(CurrencyExchangeRequest request);

    /**
     * Obtiene la tasa de cambio entre dos monedas.
     *
     * @param sourceCurrency Código de moneda origen
     * @param targetCurrency Código de moneda destino
     * @return Respuesta con información de la tasa de cambio
     */
    Mono<ExchangeRateResponse> getExchangeRateInfo(String sourceCurrency, String targetCurrency);

    /**
     * Obtiene el historial de conversiones realizadas.
     *
     * @param sourceCurrency Filtro opcional por moneda origen
     * @param targetCurrency Filtro opcional por moneda destino
     * @param startDate Filtro opcional por fecha de inicio
     * @param endDate Filtro opcional por fecha de fin
     * @return Flujo de conversiones que cumplen con los filtros
     */
    Flux<ConversionHistoryResponse> getConversionHistory(
            String sourceCurrency,
            String targetCurrency,
            LocalDateTime startDate,
            LocalDateTime endDate);
}
