package media.fynema.api.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey accessSecret;
    private final SecretKey refreshSecret;
    private final long expiration;

    public JwtService(
            @Value("${security.jwt.accessSecret}") String accessSecret,
            @Value("${security.jwt.refreshSecret}") String refreshSecret,
            @Value("${security.jwt.expiration}") long expiration
    ) {
        this.accessSecret = Keys.hmacShaKeyFor(accessSecret.getBytes());
        this.refreshSecret = Keys.hmacShaKeyFor(refreshSecret.getBytes());
        this.expiration = expiration;
    }

    public String generateAccessToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(accessSecret)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration * 10);

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(refreshSecret)
                .compact();
    }

    public Long extractUserId(String token) {
        String subject = parseToken(token).getSubject();
        return Long.valueOf(subject);
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            parseToken(accessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parser()
                    .verifyWith(refreshSecret)
                    .build()
                    .parseSignedClaims(refreshToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(accessSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
