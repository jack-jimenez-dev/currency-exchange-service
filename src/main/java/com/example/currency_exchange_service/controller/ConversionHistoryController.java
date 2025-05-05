package com.example.currency_exchange_service.controller;

import com.example.currency_exchange_service.dto.ConversionHistoryResponse;
import com.example.currency_exchange_service.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * Controlador para operaciones relacionadas con el historial de conversiones.
 * Expone endpoints REST para consultar el historial de conversiones realizadas.
 */
@RestController
@RequestMapping("/api/v1/conversion-history")
@RequiredArgsConstructor
@Slf4j
public class ConversionHistoryController {

    private final CurrencyExchangeService currencyExchangeService;

    /**
     * Endpoint para obtener el historial de conversiones con filtros opcionales.
     *
     * @param sourceCurrency Filtro opcional por moneda origen
     * @param targetCurrency Filtro opcional por moneda destino
     * @param startDate Filtro opcional por fecha de inicio
     * @param endDate Filtro opcional por fecha de fin
     * @return Flujo de conversiones que cumplen con los filtros
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ConversionHistoryResponse> getConversionHistory(
            @RequestParam(required = false) String sourceCurrency,
            @RequestParam(required = false) String targetCurrency,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.info("Solicitud de historial de conversiones recibida con filtros: sourceCurrency={}, targetCurrency={}, startDate={}, endDate={}",
                sourceCurrency, targetCurrency, startDate, endDate);
        
        return currencyExchangeService.getConversionHistory(sourceCurrency, targetCurrency, startDate, endDate);
    }
}
