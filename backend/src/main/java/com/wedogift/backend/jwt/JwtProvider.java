package com.wedogift.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JwtProvider {


    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration-in-ms}")
    private long validityInMilliseconds;


    public String issueToken(String subject) {
        return issueToken(subject, Map.of());
    }

    public String issueToken(String subject, String scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(String subject, Map<String, Object> claims) {

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer("https://wedoostore.com")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(validityInMilliseconds, ChronoUnit.MILLIS)))
                .signWith(getSigningKey())
                .compact();


    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    public String getSubject(String jwtToken) {
        return getClaims(jwtToken).getSubject();
    }

    private Claims getClaims(String jwtToken) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseSignedClaims(jwtToken).getPayload();
    }

    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);
        return subject.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return getClaims(jwt).getExpiration().before(Date.from(Instant.now()));
    }
}
