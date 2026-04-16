package com.imperiumfitness.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey key;
    //private final SecretKey key = Keys.hmacShaKeyFor("CHANGE_ME_TO_A_LONG_RANDOM_SECRET_CHANGE_ME".getBytes(StandardCharsets.UTF_8));
    
    public JwtService(@Value("${app.jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    

    public String issueToken(String userId, String username, List<String> roles) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(username) // principal = username
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(1800)))
        .claims(Map.of(
            "userId", userId,
            "roles", roles
        ))
        .signWith(key)
        .compact();
}


    public Claims parse(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
