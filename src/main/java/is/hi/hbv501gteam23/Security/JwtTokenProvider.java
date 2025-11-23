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
     * Generates a signed access JWT for the given user.
     * <p>
     * The token uses the user's email as the subject, includes the user's role as a claim,
     * sets the issued-at and expiration timestamps based on {@code jwtExpirationMillis},
     * and is signed using the configured HMAC-SHA-256 secret key.
     *
     * @param userDetails the authenticated user's details used to populate the token claims
     * @return a compact serialized JWT string representing the access token
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
     * Generates a signed refresh JWT for the given user.
     * <p>
     * The refresh token uses the user's email as the subject and has a longer lifetime
     * than the access token (configured here as {@code jwtExpirationMillis * 10}).
     * No additional custom claims are added.
     *
     * @param user the user for whom the refresh token is generated
     * @return a compact serialized JWT string representing the refresh token
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
     * Extracts the subject (username/email) from the given JWT.
     * <p>
     * The token is parsed and validated using the configured signing key, and the
     * {@code sub} (subject) claim is returned.
     *
     * @param token the JWT from which to extract the subject
     * @return the subject stored in the token, typically the user's email
     * @throws io.jsonwebtoken.JwtException if the token cannot be parsed or is invalid
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
     * Validates the given JWT token.
     * <p>
     * The token is parsed and its signature and structure are verified using the configured key.
     *
     * @param token the JWT to validate
     * @return {@code true} if the token is well-formed and passes signature verification;
     *         {@code false} if it is invalid or cannot be parsed
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

    /**
     * Convenience method to extract the email from a JWT token.
     * <p>
     * This delegates to {@link #getUsernameFromToken(String)}, since the subject
     * of the token is the user's email in this application.
     *
     * @param token the JWT from which to extract the email
     * @return the email stored as the subject of the token
     */
    public String getEmailFromToken(String token) {
        return getUsernameFromToken(token);
    }
}
