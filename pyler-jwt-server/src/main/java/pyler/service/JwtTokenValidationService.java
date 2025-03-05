package pyler.service;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pyler.config.JwtKeyProvider;
import pyler.enums.ErrorCode;
import pyler.exception.AuthException;

/**
 * JWT token 유효성 검증 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenValidationService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtKeyProvider jwtKeyProvider;

    /**
     * JWT Token 검증
     * @param token
     * @return true | false
     */
    public boolean validatetoken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtKeyProvider.getSignKey())
                    .build()
                    .parseClaimsJws(token);

            String blackList = redisTemplate.opsForValue().get("BL:" + token);
            if (blackList != null) {
                log.info("블랙리스트에 등록된 Token: {}", token);
                return false;
            }

            return true;

        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
            throw new AuthException(ErrorCode.INVALID_TOKEN, "잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            throw new AuthException(ErrorCode.INVALID_TOKEN, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
            throw new AuthException(ErrorCode.INVALID_TOKEN, "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
            throw new AuthException(ErrorCode.INVALID_TOKEN, "JWT 토큰이 잘못되었습니다.");
        } catch (Exception e) {
            log.error("JWT Exception Error: {}", e.getMessage());
            throw new AuthException(ErrorCode.INVALID_TOKEN, "JWt Exception Error");
        }
    }

    /**
     * Redis내의 Refresh Token 검증
     * @param userId
     * @param refreshToken
     * @return true | false
     */
    public boolean validateRefreshToken(Long userId, String refreshToken) {
        String storedRefreshToken = redisTemplate.opsForValue().get("RT:" + userId);
        return storedRefreshToken != null && storedRefreshToken.equals(refreshToken);
    }
}
