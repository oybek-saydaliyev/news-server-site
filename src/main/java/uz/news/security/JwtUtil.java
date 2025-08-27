package uz.news.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.news.exception.JwtTokenExpiredException;
import uz.news.exception.JwtTokenInvalidException;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret.access}")
    private String secretAccess;
    @Value("${jwt.secret.refresh}")
    private String secretRefresh;

    @Value("${jwt.expiration.access}")
    private Long expirationAccess;
    @Value("${jwt.expiration.refresh}")
    private Long expirationRefresh;


    private SecretKey getSignInKey(String key) {
        byte[] decode = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(decode);
    }

    private String generateToken(String username, String key, Long expiration) {
        return Jwts.builder()
                .subject(username)
                .signWith(getSignInKey(key))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, secretRefresh, expirationRefresh);
    }

    public String generateAccessToken(String username) {
        return generateToken(username, secretAccess, expirationAccess);
    }

    public boolean isValidToken(String token, String username) {
        try {
            final String tokenUsername = getUsernameFromAccessToken(token);
            return (tokenUsername.equals(username) && !isTokenExpired(token, secretAccess));
        } catch (JwtTokenExpiredException | JwtTokenInvalidException e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean isRefreshTokenExpired(String token) {
        try {
            return isTokenExpired(token, secretRefresh);
        } catch (JwtTokenExpiredException e) {
            return true;
        }
    }

    public String getUsernameFromToken(String token, String key) {
        String username = null;
        try {
            username = getClaimsFromToken(token, key).getSubject();
        } catch (ExpiredJwtException ex) {
            Claims claims = ex.getClaims();
            username = claims.getSubject();
        }
        return username;
    }

    public String getUsernameFromRefreshToken(String token) {
        return getUsernameFromToken(token, secretRefresh);
    }

    public String getUsernameFromAccessToken(String token) {
        return getUsernameFromToken(token, secretAccess);
    }

    private boolean isTokenExpired(String token, String key) {
        return getClaimsFromToken(token, key).getExpiration().before(new Date(System.currentTimeMillis()));
    }

    private Claims getClaimsFromToken(String token, String key) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey(key))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage());
            throw new JwtTokenExpiredException("Token muddati tugagan");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT: {}", e.getMessage());
            throw new JwtTokenInvalidException("Qo'llab-quvvatlanmaydigan token");
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT: {}", e.getMessage());
            throw new JwtTokenInvalidException("Noto'g'ri formatdagi token");
        } catch (JwtException e) {
            log.error("JWT error: {}", e.getMessage());
            throw new JwtTokenInvalidException("Token xatosi");
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new JwtTokenInvalidException("Kutilmagan xatolik");
        }
    }

    public Long getExpirationTime() {
        return expirationAccess / 1000;
    }
}
