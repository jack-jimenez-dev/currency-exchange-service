package com.example.currency_exchange_service.repository;

import com.example.currency_exchange_service.model.Currency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CurrencyRepository extends ReactiveCrudRepository<Currency, Long> {
    Mono<Currency> findByCode(String code);
}
