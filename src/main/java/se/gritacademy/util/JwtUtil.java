package se.gritacademy.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    /** Generate JWT Token with expiration and signature */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Validate JWT Token */
    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new JwtException("Token expired");
        } catch (UnsupportedJwtException | MalformedJwtException ex) {
            throw new JwtException("Invalid token");
        } catch (Exception ex) {
            throw new JwtException("Token validation failed");
        }
    }
}
