package is.hi.hbv501gteam23.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final SecretKey key;
    private final long jwtExpirationMillis;

    public JwtTokenProvider(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") long jwtExpirationMillis
    ) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(decodedKey);
        this.jwtExpirationMillis = jwtExpirationMillis;
    }

    /**
     * Generates a token with username as subject, an expiry matchDate,
     * and HMAC-SHA-256 to sign
     * @param userDetails the details for the subject
     * @return
     */
    public String generateAccessToken(CustomUserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtExpirationMillis);

        return Jwts.builder()
            .subject(userDetails.getEmail())
            .claim("role", userDetails.getRole())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .signWith(key)
            .compact();
    }

    /**
     *
     * @param user
     * @return
     */
    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtExpirationMillis * 10);

        return Jwts.builder()
            .subject(user.getEmail())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .signWith(key)
            .compact();
    }

    /**
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claims.getSubject();
    }

    /**
     * Validates the JWT token
     * @param token the token to validate
     * @return false if invalid, otherwise true
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return getUsernameFromToken(token);
    }
}
