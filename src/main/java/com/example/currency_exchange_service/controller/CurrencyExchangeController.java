package com.example.currency_exchange_service.controller;

import com.example.currency_exchange_service.dto.CurrencyExchangeRequest;
import com.example.currency_exchange_service.dto.CurrencyExchangeResponse;
import com.example.currency_exchange_service.service.CurrencyExchangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controlador para operaciones de cambio de moneda.
 * Expone endpoints REST para realizar conversiones de moneda.
 */
@RestController
@RequestMapping("/api/v1/currency-exchange")
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    /**
     * Endpoint para convertir moneda.
     *
     * @param request Solicitud de conversión con monto, moneda origen y moneda destino
     * @return Respuesta con el monto convertido y tasa de cambio aplicada
     */
    @PostMapping("/convert")
    public Mono<ResponseEntity<CurrencyExchangeResponse>> convertCurrency(@Valid @RequestBody CurrencyExchangeRequest request) {
        log.info("Solicitud de conversión recibida: {}", request);
        return currencyExchangeService.convertCurrency(request)
                .map(ResponseEntity::ok);
    }
}
