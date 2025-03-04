package pyler.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pyler.domain.dto.TokenDTO;
import pyler.enums.ErrorCode;
import pyler.exception.AuthException;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JWT Token 관리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenManageService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenCreateService jwtTokenCreateService;
    private final JwtTokenValidationService jwtTokenValidationService;
    private final JwtTokenExtractionService jwtTokenExtractionService;

    /**
     * Refresh Token을 통해, new Access Token 발급
     * @param refreshToken
     * @return TokenDTO(new Access Token)
     */
    public TokenDTO refreshAccessToken(String refreshToken) {
        jwtTokenValidationService.validatetoken(refreshToken); //Refresh Token 검증

        Long userId = jwtTokenExtractionService.getUserId(refreshToken); //Refresh Token을 통해 정보 추출

        // Redis에 저장된 Refresh Token 검증
        if (!jwtTokenValidationService.validateRefreshToken(userId, refreshToken)) {
            throw new AuthException(ErrorCode.INVALID_TOKEN, "유효하지 않은 Refresh Token");
        }

        // Claim 내 정보 조회
        Claims claims = jwtTokenExtractionService.extractAllClaims(refreshToken);
        String userEmail = claims.get("userEmail", String.class);
        Boolean isMaster = claims.get("isMaster", Boolean.class);

        //신규 Access Token 발급
        String newAccessToken = jwtTokenCreateService.createAccessToken(userId, userEmail, isMaster);

        return TokenDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * JWT Token 무효화
     * @param accessToken
     */
    public void invalidationToken(String accessToken) {
        try {
            jwtTokenValidationService.validatetoken(accessToken); //Token 검증

            // Access Token내부의 userId
            Long userId = jwtTokenExtractionService.getUserId(accessToken);

            // Access Token 만료 시간
            Date expiration = jwtTokenExtractionService.extractAllClaims(accessToken).getExpiration();
            Date nowDate = new Date();
            long ttl = expiration.getTime() - nowDate.getTime();

            if (ttl > 0) {
                //redis에 만료할 Access Token을 BlackList로 등록(BL)
                redisTemplate.opsForValue().set(
                        "BL:" + accessToken,
                        userId.toString(),
                        ttl,
                        TimeUnit.MILLISECONDS
                );

                // Refresh Token 삭제
                redisTemplate.delete("RT:" + userId);

                log.info("JWT Token 무효화 Success. userID: {}", userId);

            } else {
                log.info("이미 만료된 JWt Token. userID: {}", userId);
            }
        } catch (Exception e) {
            log.error("Token Invalidation Exception Error: {}", e.getMessage());
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR, "Token 무효화 도중 오류 발생");
        }


    }
}
