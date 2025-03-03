package pyler.service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import pyler.config.JwtKeyProvider;
import pyler.domain.dto.TokenDTO;
import pyler.exception.AuthException;

import javax.crypto.SecretKey;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenValidationServiceTest {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenValidationServiceTest.class);
    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private JwtKeyProvider jwtKeyProvider;

    @InjectMocks
    private JwtTokenCreateService jwtTokenCreateService;

    @InjectMocks
    private JwtTokenManageService jwtTokenManageService;

    @InjectMocks
    private JwtTokenValidationService jwtTokenValidationService;

    private SecretKey secretKey;
    private String expiredToken;
    private String accessToken;
    private String refreshToken;
    private TokenDTO tokenDTO;

    @BeforeEach
    void setUp(){
        //Test SecretKey 생성
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS384);

        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        lenient().when(jwtKeyProvider.getSignKey()).thenReturn(secretKey);
        lenient().when(valueOperations.get(anyString())).thenReturn(null);

        long userId = 1L;
        String userEmail = "joseph@gmail.com";
        Integer userRole = 1;

        accessToken = Jwts.builder()
                .setSubject("1")
                .claim("userEmail", userEmail)
                .claim("userRole", userRole)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(secretKey, SignatureAlgorithm.HS384)
                .compact();

        expiredToken = Jwts.builder()
                .setSubject("1")
                .claim("userEmail", userEmail)
                .claim("userRole", 2)
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000))
                .setExpiration(new Date(System.currentTimeMillis() - 3600000))
                .compact();

//        tokenDTO = jwtTokenCreateService.createToken(userId, userEmail, userRole);
//        accessToken = tokenDTO.getAccessToken();
//        refreshToken = tokenDTO.getRefreshToken();

    }

    @DisplayName("토큰 유효성 테스트")
    @Test
    void vaildationToeknTest() {

        //when
        log.info(accessToken);
        boolean result = jwtTokenValidationService.validatetoken(accessToken);

        //then
        assertThat(result).isTrue();

    }

    @DisplayName("만료된 토큰 검증")
    @Test
    void expiredTokenTest() {
        assertThatThrownBy(() -> jwtTokenValidationService.validatetoken(expiredToken))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining("만료된 JWt Token");
    }

}