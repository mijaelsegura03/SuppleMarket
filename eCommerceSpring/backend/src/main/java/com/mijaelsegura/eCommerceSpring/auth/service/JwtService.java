package com.mijaelsegura.eCommerceSpring.auth.service;

import com.mijaelsegura.eCommerceSpring.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.token-expiration}")
    private long TOKEN_EXPIRATION;

    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignatureKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); //se convierte la secret_key en un formato adecuado para HMAC SHA
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .claims(Map.of(
                        "USERNAME", user.getUsernameString(),
                        "ROLE", user.getRole()
                ))
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .signWith(getSignatureKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractTokenExpiration(token).before(new Date());
    }

    private Date extractTokenExpiration(String token) {
        return Jwts.parser().verifyWith(getSignatureKey()).build().parseSignedClaims(token).getPayload().getExpiration();
    }

}
