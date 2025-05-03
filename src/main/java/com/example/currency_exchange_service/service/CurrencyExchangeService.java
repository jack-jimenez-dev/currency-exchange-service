package com.example.currency_exchange_service.service;

import com.example.currency_exchange_service.dto.CurrencyExchangeRequest;
import com.example.currency_exchange_service.dto.CurrencyExchangeResponse;
import reactor.core.publisher.Mono;

public interface CurrencyExchangeService {
    Mono<CurrencyExchangeResponse> convertCurrency(CurrencyExchangeRequest request);
}
