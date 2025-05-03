package com.example.currency_exchange_service.service;

import com.example.currency_exchange_service.dto.CurrencyExchangeRequest;
import com.example.currency_exchange_service.dto.CurrencyExchangeResponse;
import com.example.currency_exchange_service.exception.CurrencyNotFoundException;
import com.example.currency_exchange_service.exception.ExchangeRateNotFoundException;
import com.example.currency_exchange_service.model.ExchangeRate;
import com.example.currency_exchange_service.repository.CurrencyRepository;
import com.example.currency_exchange_service.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación del servicio de conversión de monedas.
 * Proporciona funcionalidad para convertir montos entre diferentes monedas
 * con caché de resultados y patrones de resiliencia.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    // Caché manual usando ConcurrentHashMap
    private final Map<String, CachedResponse> cache = new ConcurrentHashMap<>();

    // Clase para almacenar respuestas en caché con timestamp
    private static class CachedResponse {
        private final CurrencyExchangeResponse response;
        private final LocalDateTime timestamp;

        public CachedResponse(CurrencyExchangeResponse response) {
            this.response = response;
            this.timestamp = LocalDateTime.now();
        }

        public boolean isExpired() {
            // Expirar después de 5 minutos (300 segundos)
            return Duration.between(timestamp, LocalDateTime.now()).getSeconds() > 300;
        }
    }

    /**
     * Convierte un monto de una moneda a otra.
     * Los resultados se almacenan en caché durante 5 minutos para optimizar operaciones repetidas.
     *
     * @param request Solicitud con monto, moneda origen y moneda destino
     * @return Respuesta con el monto convertido y la tasa de cambio aplicada
     */
    @Override
    public Mono<CurrencyExchangeResponse> convertCurrency(CurrencyExchangeRequest request) {
        log.info("Convirtiendo {} {} a {}", request.getAmount(), request.getSourceCurrency(), request.getTargetCurrency());

        // Clave para la caché
        String cacheKey = request.getSourceCurrency() + "_" + request.getTargetCurrency() + "_" + request.getAmount();

        // Verificar si existe en caché y no ha expirado
        CachedResponse cachedResponse = cache.get(cacheKey);
        if (cachedResponse != null && !cachedResponse.isExpired()) {
            log.info("Usando respuesta en caché para: {}", cacheKey);
            return Mono.just(cachedResponse.response);
        }

        // Si no está en caché o ha expirado, realizar la conversión
        return validateCurrencies(request.getSourceCurrency(), request.getTargetCurrency())
                .then(getExchangeRate(request.getSourceCurrency(), request.getTargetCurrency()))
                .map(exchangeRate -> calculateConversion(request.getAmount(), exchangeRate, request))
                .doOnNext(response -> {
                    // Guardar en caché
                    cache.put(cacheKey, new CachedResponse(response));
                    log.info("Guardando respuesta en caché para: {}", cacheKey);
                });
    }

    /**
     * Valida que las monedas origen y destino existan en la base de datos.
     *
     * @param sourceCurrency Código de moneda origen
     * @param targetCurrency Código de moneda destino
     * @return Mono vacío si ambas monedas son válidas
     */
    private Mono<Void> validateCurrencies(String sourceCurrency, String targetCurrency) {
        return currencyRepository.findByCode(sourceCurrency)
                .switchIfEmpty(Mono.error(new CurrencyNotFoundException("Moneda origen no encontrada: " + sourceCurrency)))
                .then(currencyRepository.findByCode(targetCurrency)
                        .switchIfEmpty(Mono.error(new CurrencyNotFoundException("Moneda destino no encontrada: " + targetCurrency))))
                .then();
    }

    /**
     * Obtiene la tasa de cambio entre dos monedas.
     *
     * @param sourceCurrency Código de moneda origen
     * @param targetCurrency Código de moneda destino
     * @return Tasa de cambio entre las monedas
     */
    private Mono<ExchangeRate> getExchangeRate(String sourceCurrency, String targetCurrency) {
        return exchangeRateRepository.findBySourceCurrencyCodeAndTargetCurrencyCode(sourceCurrency, targetCurrency)
                .switchIfEmpty(Mono.error(new ExchangeRateNotFoundException(
                        "Tasa de cambio no encontrada para " + sourceCurrency + " a " + targetCurrency)));
    }

    /**
     * Calcula la conversión de moneda aplicando la tasa de cambio.
     *
     * @param amount Monto a convertir
     * @param exchangeRate Tasa de cambio a aplicar
     * @param request Solicitud original
     * @return Respuesta con el monto convertido
     */
    private CurrencyExchangeResponse calculateConversion(BigDecimal amount, ExchangeRate exchangeRate, CurrencyExchangeRequest request) {
        BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate()).setScale(2, RoundingMode.HALF_UP);

        return CurrencyExchangeResponse.builder()
                .amount(amount)
                .convertedAmount(convertedAmount)
                .sourceCurrency(request.getSourceCurrency())
                .targetCurrency(request.getTargetCurrency())
                .exchangeRate(exchangeRate.getRate())
                .build();
    }
}
