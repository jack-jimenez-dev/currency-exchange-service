package com.example.currency_exchange_service.controller;

import com.example.currency_exchange_service.dto.ExchangeRateResponse;
import com.example.currency_exchange_service.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controlador para operaciones relacionadas con tasas de cambio.
 * Expone endpoints REST para consultar tasas de cambio.
 */
@RestController
@RequestMapping("/api/v1/exchange-rates")
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateController {

    private final CurrencyExchangeService currencyExchangeService;

    /**
     * Endpoint para obtener información detallada sobre una tasa de cambio.
     *
     * @param sourceCurrency Código de moneda origen
     * @param targetCurrency Código de moneda destino
     * @return Respuesta con información de la tasa de cambio
     */
    @GetMapping("/{sourceCurrency}/{targetCurrency}")
    public Mono<ResponseEntity<ExchangeRateResponse>> getExchangeRateInfo(
            @PathVariable String sourceCurrency,
            @PathVariable String targetCurrency) {
        
        log.info("Solicitud de información de tasa de cambio recibida: {} a {}", sourceCurrency, targetCurrency);
        return currencyExchangeService.getExchangeRateInfo(sourceCurrency, targetCurrency)
                .map(ResponseEntity::ok);
    }
}
