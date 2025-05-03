package com.example.currency_exchange_service.controller;

import com.example.currency_exchange_service.dto.CurrencyExchangeRequest;
import com.example.currency_exchange_service.dto.CurrencyExchangeResponse;
import com.example.currency_exchange_service.exception.CurrencyNotFoundException;
import com.example.currency_exchange_service.service.CurrencyExchangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para el controlador de conversión de monedas.
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(CurrencyExchangeController.class)
class CurrencyExchangeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CurrencyExchangeService currencyExchangeService;

    /**
     * Prueba la conversión exitosa de moneda.
     */
    @Test
    void convertCurrency_Success() {
        // Dado
        CurrencyExchangeRequest request = CurrencyExchangeRequest.builder()
                .amount(new BigDecimal("100"))
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .build();

        CurrencyExchangeResponse response = CurrencyExchangeResponse.builder()
                .amount(new BigDecimal("100"))
                .convertedAmount(new BigDecimal("93.00"))
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .exchangeRate(new BigDecimal("0.93"))
                .build();

        when(currencyExchangeService.convertCurrency(any(CurrencyExchangeRequest.class)))
                .thenReturn(Mono.just(response));

        // Cuando y Entonces
        webTestClient.post()
                .uri("/api/v1/currency-exchange/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CurrencyExchangeResponse.class)
                .isEqualTo(response);
    }

    /**
     * Prueba el error de validación cuando el monto es negativo.
     */
    @Test
    void convertCurrency_ValidationError() {
        // Dado
        CurrencyExchangeRequest request = CurrencyExchangeRequest.builder()
                .amount(new BigDecimal("-100"))  // Inválido: monto negativo
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .build();

        // Cuando y Entonces
        webTestClient.post()
                .uri("/api/v1/currency-exchange/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * Prueba el error cuando no se encuentra la moneda.
     */
    @Test
    void convertCurrency_CurrencyNotFound() {
        // Dado
        CurrencyExchangeRequest request = CurrencyExchangeRequest.builder()
                .amount(new BigDecimal("100"))
                .sourceCurrency("XYZ")  // Código de moneda inválido
                .targetCurrency("EUR")
                .build();

        when(currencyExchangeService.convertCurrency(any(CurrencyExchangeRequest.class)))
                .thenReturn(Mono.error(new CurrencyNotFoundException("Moneda origen no encontrada: XYZ")));

        // Cuando y Entonces
        webTestClient.post()
                .uri("/api/v1/currency-exchange/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound();
    }
}
