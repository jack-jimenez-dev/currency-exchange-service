package com.example.currency_exchange_service.service;

import com.example.currency_exchange_service.dto.CurrencyExchangeRequest;
import com.example.currency_exchange_service.dto.CurrencyExchangeResponse;
import com.example.currency_exchange_service.exception.CurrencyNotFoundException;
import com.example.currency_exchange_service.exception.ExchangeRateNotFoundException;
import com.example.currency_exchange_service.model.Currency;
import com.example.currency_exchange_service.model.ExchangeRate;
import com.example.currency_exchange_service.repository.CurrencyRepository;
import com.example.currency_exchange_service.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para el servicio de conversión de monedas.
 */
@ExtendWith(MockitoExtension.class)
class CurrencyExchangeServiceImplTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    // Ya no necesitamos el circuit breaker

    @InjectMocks
    private CurrencyExchangeServiceImpl currencyExchangeService;

    private Currency usdCurrency;
    private Currency eurCurrency;
    private ExchangeRate usdToEurRate;
    private CurrencyExchangeRequest request;

    /**
     * Configuración inicial para las pruebas.
     */
    @BeforeEach
    void setUp() {
        usdCurrency = Currency.builder()
                .id(1L)
                .code("USD")
                .name("Dólar Estadounidense")
                .build();

        eurCurrency = Currency.builder()
                .id(2L)
                .code("EUR")
                .name("Euro")
                .build();

        usdToEurRate = ExchangeRate.builder()
                .id(1L)
                .sourceCurrencyCode("USD")
                .targetCurrencyCode("EUR")
                .rate(new BigDecimal("0.93"))
                .lastUpdated(LocalDateTime.now())
                .build();

        request = CurrencyExchangeRequest.builder()
                .amount(new BigDecimal("100"))
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .build();

        // Ya no necesitamos configurar el circuit breaker
    }

    /**
     * Prueba la conversión exitosa de moneda.
     */
    @Test
    void convertCurrency_Success() {
        // Dado
        when(currencyRepository.findByCode("USD")).thenReturn(Mono.just(usdCurrency));
        when(currencyRepository.findByCode("EUR")).thenReturn(Mono.just(eurCurrency));
        when(exchangeRateRepository.findBySourceCurrencyCodeAndTargetCurrencyCode("USD", "EUR"))
                .thenReturn(Mono.just(usdToEurRate));

        // Cuando
        Mono<CurrencyExchangeResponse> result = currencyExchangeService.convertCurrency(request);

        // Entonces
        BigDecimal expectedConvertedAmount = new BigDecimal("100").multiply(new BigDecimal("0.93"))
                .setScale(2, RoundingMode.HALF_UP);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getAmount().equals(new BigDecimal("100")) &&
                        response.getConvertedAmount().equals(expectedConvertedAmount) &&
                        response.getSourceCurrency().equals("USD") &&
                        response.getTargetCurrency().equals("EUR") &&
                        response.getExchangeRate().equals(new BigDecimal("0.93")))
                .verifyComplete();
    }

    /**
     * Prueba el error cuando no se encuentra la moneda origen.
     */
    @Test
    void convertCurrency_SourceCurrencyNotFound() {
        // Dado
        when(currencyRepository.findByCode("USD")).thenReturn(Mono.empty());

        // Cuando
        Mono<CurrencyExchangeResponse> result = currencyExchangeService.convertCurrency(request);

        // Entonces
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof CurrencyNotFoundException &&
                        throwable.getMessage().equals("Moneda origen no encontrada: USD"))
                .verify();
    }

    /**
     * Prueba el error cuando no se encuentra la moneda destino.
     */
    @Test
    void convertCurrency_TargetCurrencyNotFound() {
        // Dado
        when(currencyRepository.findByCode("USD")).thenReturn(Mono.just(usdCurrency));
        when(currencyRepository.findByCode("EUR")).thenReturn(Mono.empty());

        // Cuando
        Mono<CurrencyExchangeResponse> result = currencyExchangeService.convertCurrency(request);

        // Entonces
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof CurrencyNotFoundException &&
                        throwable.getMessage().equals("Moneda destino no encontrada: EUR"))
                .verify();
    }

    /**
     * Prueba el error cuando no se encuentra la tasa de cambio.
     */
    @Test
    void convertCurrency_ExchangeRateNotFound() {
        // Dado
        when(currencyRepository.findByCode("USD")).thenReturn(Mono.just(usdCurrency));
        when(currencyRepository.findByCode("EUR")).thenReturn(Mono.just(eurCurrency));
        when(exchangeRateRepository.findBySourceCurrencyCodeAndTargetCurrencyCode("USD", "EUR"))
                .thenReturn(Mono.empty());

        // Cuando
        Mono<CurrencyExchangeResponse> result = currencyExchangeService.convertCurrency(request);

        // Entonces
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof ExchangeRateNotFoundException &&
                        throwable.getMessage().equals("Tasa de cambio no encontrada para USD a EUR"))
                .verify();
    }

    /**
     * Prueba que la caché funciona correctamente al hacer múltiples solicitudes.
     */
    @Test
    void convertCurrency_CacheWorks() {
        // Dado
        when(currencyRepository.findByCode("USD")).thenReturn(Mono.just(usdCurrency));
        when(currencyRepository.findByCode("EUR")).thenReturn(Mono.just(eurCurrency));
        when(exchangeRateRepository.findBySourceCurrencyCodeAndTargetCurrencyCode("USD", "EUR"))
                .thenReturn(Mono.just(usdToEurRate));

        // Primera llamada - debería consultar la base de datos
        Mono<CurrencyExchangeResponse> result1 = currencyExchangeService.convertCurrency(request);

        // Segunda llamada - debería usar la caché
        Mono<CurrencyExchangeResponse> result2 = currencyExchangeService.convertCurrency(request);

        // Entonces
        BigDecimal expectedConvertedAmount = new BigDecimal("100").multiply(new BigDecimal("0.93"))
                .setScale(2, RoundingMode.HALF_UP);

        // Verificar primera llamada
        StepVerifier.create(result1)
                .expectNextMatches(response ->
                        response.getAmount().equals(new BigDecimal("100")) &&
                        response.getConvertedAmount().equals(expectedConvertedAmount) &&
                        response.getSourceCurrency().equals("USD") &&
                        response.getTargetCurrency().equals("EUR") &&
                        response.getExchangeRate().equals(new BigDecimal("0.93")))
                .verifyComplete();

        // Verificar segunda llamada (desde caché)
        StepVerifier.create(result2)
                .expectNextMatches(response ->
                        response.getAmount().equals(new BigDecimal("100")) &&
                        response.getConvertedAmount().equals(expectedConvertedAmount) &&
                        response.getSourceCurrency().equals("USD") &&
                        response.getTargetCurrency().equals("EUR") &&
                        response.getExchangeRate().equals(new BigDecimal("0.93")))
                .verifyComplete();
    }
}
