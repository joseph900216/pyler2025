package pyler.service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import pyler.config.JwtKeyProvider;
import pyler.domain.dto.TokenDTO;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenCreateServiceTest {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenCreateServiceTest.class);

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private JwtKeyProvider jwtKeyProvider;

    @InjectMocks
    private JwtTokenCreateService jwtTokenCreateService;

    @BeforeEach
    void setUp(){
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS384);
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        lenient().when(jwtKeyProvider.getSignKey()).thenReturn(secretKey);
    }

    @Test
    void createToken() {
        //given
        Long userId = 1L;
        String userEmail = "joseph900216@gmail.com";
        int userRole = 2;

        //when
        TokenDTO tokenDTO = jwtTokenCreateService.createToken(userId, userEmail, userRole);

        //then
        log.info("Access Token: {}", tokenDTO.getAccessToken());
        log.info("Secret Token: {}", tokenDTO.getRefreshToken());
        assertThat(tokenDTO).as("TokenDTO Is Null").isNotNull();
        assertThat(tokenDTO).isNotNull();
        assertThat(tokenDTO.getAccessToken().isEmpty());
        assertThat(tokenDTO.getRefreshToken().isEmpty());

        Mockito.verify(valueOperations).set(
                eq("RT:" + userId),
                anyString(),
                anyLong(),
                any()
        );
    }

    @Test
    void createAccessToken() {
        //given
        Long userId = 1L;
        String userEmail="joseph900216@gmail.com";
        int userRole = 2;

        //when
        String accessToken = jwtTokenCreateService.createAccessToken(userId, userEmail, userRole);

        //then
        log.info("Access Token: {}", accessToken);
        assertThat(accessToken).isNotNull();
    }
}