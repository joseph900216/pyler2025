package pyler.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pyler.contents.RequestMap;
import pyler.domain.dto.*;
import pyler.enums.ErrorCode;
import pyler.exception.AuthException;

/**
 * 모듈 별 JWt 인증 인터셉터를 위한 Client 구성
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenClient {

    private final RestTemplate restTemplate;

    @Value("${jwt.server.url}")
    private String jwtServerUrl;

    /**
     * Client Token 생성
     * @param userId
     * @param userEmail
     * @param isMaster
     * @return
     */
    public TokenDTO createToken(Long userId, String userEmail, boolean isMaster) {
        String url = jwtServerUrl + RequestMap.api + RequestMap.v1 + RequestMap.token;

        TokenCreateReqDTO req = new TokenCreateReqDTO(userId, userEmail, isMaster);

        try {
            ResponseEntity<ApiResponse<TokenDTO>> res = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(req),
                    new ParameterizedTypeReference<ApiResponse<TokenDTO>>() {}
            );

            if (res.getBody() != null && res.getBody().getData() != null) {
                return res.getBody().getData();
            } else {
                throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR, "Token 생성 실패");
            }

        } catch (Exception e) {
            log.error("JWT API 통신 에러: {}", e.getMessage());
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR, "JWT API 통신 Error");
        }
    }

    /**
     * Client Token 검증
     * @param token
     * @return
     */
    public boolean validationToken(String token) {
        String url = jwtServerUrl + RequestMap.api + RequestMap.v1 + RequestMap.token + "/validation?token=" + token;

        try {
            ResponseEntity<ApiResponse<Boolean>> res = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<Boolean>>() {}
            );

            if (res.getBody() != null && res.getBody().getData() != null) {
                return res.getBody().getData();
            } else {
                return false;
            }

        } catch (Exception e) {
            log.error("JWT API 통신 에러: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Client Token 갱신
     * @param refreshToken
     * @return
     */
    public TokenDTO refreshToken(String refreshToken) {
        String url = jwtServerUrl + RequestMap.api + RequestMap.v1 + RequestMap.token + "/refresh";

        TokenReqDTO req = new TokenReqDTO();
        req.setRefreshToken(refreshToken);

        try {
            ResponseEntity<ApiResponse<TokenDTO>> res = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(req),
                    new ParameterizedTypeReference<ApiResponse<TokenDTO>>() {}
            );

            if (res.getBody() != null && res.getBody().getData() != null) {
                return res.getBody().getData();
            } else {
                throw new AuthException(ErrorCode.INVALID_TOKEN, "Token 갱신 실패");
            }
        } catch (Exception e) {
            log.error("JWT API 통신 에러: {}", e.getMessage());
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR, "JWT API 통신 Error");
        }
    }

    /**
     * Client Token 상세 조회
     * @param token
     * @return
     */
    public TokenInfoDTO tokenInfo(String token) {
        String url = jwtServerUrl + RequestMap.api + RequestMap.v1 + RequestMap.token +"/info?token=" + token;

        try {
            ResponseEntity<ApiResponse<TokenInfoDTO>> res = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<TokenInfoDTO>>() {}
            );

            if (res.getBody() != null && res.getBody().getData() != null) {
                return res.getBody().getData();
            } else {
                throw new AuthException(ErrorCode.INVALID_TOKEN, "Token 조회 실패");
            }
        } catch (Exception e) {
            log.error("JWT API 통신 에러: {}", e.getMessage());
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR, "JWT API 통신 Error");
        }
    }

    /**
     * Client Token 무효화
     * @param accessToken
     */
    public void invalidToken(String accessToken) {
        String url = jwtServerUrl + RequestMap.api + RequestMap.v1 + RequestMap.token +"/invalidate";

        TokenReqDTO req = new TokenReqDTO();
        req.setAccessToken(accessToken);

        try {
            restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(req),
                    Void.class
            );
        } catch (Exception e) {
            log.error("JWT API 통신 에러: {}", e.getMessage());
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR, "JWT API 통신 Error");
        }


    }


}
