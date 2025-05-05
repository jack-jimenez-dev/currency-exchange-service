package com.example.currency_exchange_service.repository;

import com.example.currency_exchange_service.model.ConversionHistory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * Repositorio para acceder a los datos del histórico de conversiones.
 */
@Repository
public interface ConversionHistoryRepository extends ReactiveCrudRepository<ConversionHistory, Long> {
    
    /**
     * Busca conversiones por moneda de origen.
     * 
     * @param sourceCurrencyCode Código de la moneda de origen
     * @return Flujo de conversiones que coinciden con la moneda de origen
     */
    Flux<ConversionHistory> findBySourceCurrencyCode(String sourceCurrencyCode);
    
    /**
     * Busca conversiones por moneda de destino.
     * 
     * @param targetCurrencyCode Código de la moneda de destino
     * @return Flujo de conversiones que coinciden con la moneda de destino
     */
    Flux<ConversionHistory> findByTargetCurrencyCode(String targetCurrencyCode);
    
    /**
     * Busca conversiones realizadas entre dos fechas.
     * 
     * @param startDate Fecha de inicio
     * @param endDate Fecha de fin
     * @return Flujo de conversiones realizadas en el rango de fechas
     */
    Flux<ConversionHistory> findByConversionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Busca conversiones realizadas por un usuario específico.
     * 
     * @param userId ID del usuario
     * @return Flujo de conversiones realizadas por el usuario
     */
    Flux<ConversionHistory> findByUserId(String userId);
}
