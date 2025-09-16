package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.models.AppUser;
import com.gravityer.ecommerce.models.AppUserPrincipal;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Service
public class JwtService {
    // Dummy Secret
    private final String SECRET = "DummySecret123qwertyuiopasdfghjklzxcvbnm1234567890";

    public String generateToken(AppUserPrincipal appUserPrincipal) {

        HashMap<String, Object> allClaims = new HashMap<>();
        allClaims.put("roles", appUserPrincipal.getUser().getRoles());

        log.info("Generating JWT for user: {} with roles {}", appUserPrincipal.getUser().getUsername(), allClaims.entrySet());

        return Jwts.builder()
                .claims(allClaims)
                .subject(appUserPrincipal.getUser().getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 mins
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
