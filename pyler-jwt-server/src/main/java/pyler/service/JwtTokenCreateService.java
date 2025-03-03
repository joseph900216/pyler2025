package pyler.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pyler.config.JwtKeyProvider;
import pyler.domain.dto.TokenDTO;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Token 생성 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenCreateService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtKeyProvider jwtKeyProvider;

    private static final long accessTokenExpire = 30 * 60 * 1000L; // access token 만료: 30분

    private static final long refreshTokenExpire = 7 * 24 * 60 * 60 * 1000L; //refresh token 만료: 7일

    /**
     * Token 생성
     * @param userId
     * @param userEmail
     * @param userRole
     * @return Token(Access Token, Refresh Token)
     */
    public TokenDTO createToken(Long userId, String userEmail, int userRole) {
        Date nowDate = new Date();
        Key key = jwtKeyProvider.getSignKey();

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userEmail", userEmail)
                .claim("userRole", userRole)
                .setIssuedAt(nowDate)
                .setExpiration(new Date(nowDate.getTime() + accessTokenExpire))
                .signWith(key, SignatureAlgorithm.HS384)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(nowDate)
                .setExpiration(new Date(nowDate.getTime() + refreshTokenExpire))
                .signWith(key, SignatureAlgorithm.HS384)
                .compact();

        // Redis 저장 (Refresh Token: Key=RT(userId), value=refreshToken)
        redisTemplate.opsForValue().set(
                "RT:" + userId,
                refreshToken,
                refreshTokenExpire,
                TimeUnit.MILLISECONDS
        );

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /***
     * 신규 Access Token 생성
     * @param userId
     * @param email
     * @param userRole
     * @return new Access Token
     */
    public String createAccessToken(Long userId, String email, int userRole) {
        Date nowDate = new Date();
        Key key = jwtKeyProvider.getSignKey();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userEmail", email)
                .claim("userRole", userRole)
                .setIssuedAt(nowDate)
                .setExpiration(new Date(nowDate.getTime() + accessTokenExpire))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
