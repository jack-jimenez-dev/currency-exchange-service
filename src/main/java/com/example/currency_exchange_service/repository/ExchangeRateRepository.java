package com.example.currency_exchange_service.repository;

import com.example.currency_exchange_service.model.ExchangeRate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate, Long> {
    Mono<ExchangeRate> findBySourceCurrencyCodeAndTargetCurrencyCode(String sourceCurrencyCode, String targetCurrencyCode);
}
