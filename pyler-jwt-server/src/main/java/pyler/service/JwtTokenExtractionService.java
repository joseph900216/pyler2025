package pyler.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pyler.config.JwtKeyProvider;
import pyler.enums.ErrorCode;
import pyler.exception.AuthException;

/**
 * JWT Token 정보 Parsing 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenExtractionService {

    private final JwtKeyProvider jwtKeyProvider;

    /**
     * Token내부의 모든 Claim 정보 Parsing
     * @param token
     * @return Claim 정보
     */
    public Claims extractAllClaims(String token) {
        try {

            return Jwts.parserBuilder()
                    .setSigningKey(jwtKeyProvider.getSignKey())
                    .build()
                    .parseClaimsJwt(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            log.error("Token 만료");
            throw new AuthException(ErrorCode.INVALID_TOKEN, "만료된 JWt Token");
        } catch (Exception e) {
            log.error("JWT Exception Error: {}", e.getMessage());
            throw new AuthException(ErrorCode.INVALID_TOKEN, "JWt Exception Error");
        }
    }

    /**
     * Token 내부의 userId 추출
     * @param token
     * @return userId
     */
    public Long getUserId(String token) {
        Claims claims = extractAllClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    /**
     * Token 내부의 user email 추출
     * @param token
     * @return userEmail
     */
    public String getUserEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userEmail", String.class);
    }

    /**
     * Token 내부의 user role 추출
     * @param token
     * @return userRole
     */
    public Integer getUserRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userRole", Integer.class);
    }

    /**
     * Token 내부의 만료 시간 추출
     * @param token
     * @return token Expire Time
     */
    public long getExpireTime(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration().getTime();
    }
}
