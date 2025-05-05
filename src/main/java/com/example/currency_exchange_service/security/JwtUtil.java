package com.example.currency_exchange_service.security;

import com.example.currency_exchange_service.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilidad para generar y validar tokens JWT.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret:mysecretkey12345678901234567890}")
    private String secret;

    @Value("${jwt.expiration:86400}")
    private long expirationTime;

    /**
     * Genera un token JWT para un usuario.
     * 
     * @param user Usuario para el que se genera el token
     * @return Token JWT generado
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida un token JWT.
     * 
     * @param token Token JWT a validar
     * @param userDetails Detalles del usuario
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrae el nombre de usuario de un token JWT.
     * 
     * @param token Token JWT
     * @return Nombre de usuario
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración de un token JWT.
     * 
     * @param token Token JWT
     * @return Fecha de expiración
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae una reclamación específica de un token JWT.
     * 
     * @param token Token JWT
     * @param claimsResolver Función para extraer la reclamación
     * @return Reclamación extraída
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todas las reclamaciones de un token JWT.
     * 
     * @param token Token JWT
     * @return Todas las reclamaciones
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica si un token JWT ha expirado.
     * 
     * @param token Token JWT
     * @return true si el token ha expirado, false en caso contrario
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Obtiene la clave de firma para el token JWT.
     * 
     * @return Clave de firma
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
