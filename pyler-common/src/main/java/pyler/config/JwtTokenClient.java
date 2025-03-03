package pyler.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pyler.contents.RequestMap;
import pyler.domain.dto.*;
import pyler.enums.ErrorCode;
import pyler.exception.AuthException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenClient {

    private final RestTemplate restTemplate;

    @Value("${jwt.server.url}")
    private String jwtServerUrl;

    /**
     * Client Token 생성
     * @param userId
     * @param userEmail
     * @param userRole
     * @return
     */
    public TokenDTO createToken(Long userId, String userEmail, int userRole) {
        String url = jwtServerUrl + RequestMap.api + RequestMap.v1 + RequestMap.token;

        TokenCreateReqDTO req = new TokenCreateReqDTO(userId, userEmail, userRole);

        try {
            ResponseEntity<ApiResponse<TokenDTO>> res = restTemplate.postForEntity(
                    url,
                    req,
                    (Class<ApiResponse<TokenDTO>>) (Class<?>) ApiResponse.class
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
            ResponseEntity<ApiResponse<Boolean>> res = restTemplate.getForEntity(
                    url,
                    (Class<ApiResponse<Boolean>>) (Class<?>) ApiResponse.class
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
            ResponseEntity<ApiResponse<TokenDTO>> res = restTemplate.postForEntity(
                    url,
                    req,
                    (Class<ApiResponse<TokenDTO>>) (Class<?>) ApiResponse.class
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
            ResponseEntity<ApiResponse<TokenInfoDTO>> res = restTemplate.getForEntity(
                    url,
                    (Class<ApiResponse<TokenInfoDTO>>) (Class<?>) ApiResponse.class
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
            restTemplate.postForEntity(url, req, Void.class);
        } catch (Exception e) {
            log.error("JWT API 통신 에러: {}", e.getMessage());
            throw new AuthException(ErrorCode.INTERNAL_SERVER_ERROR, "JWT API 통신 Error");
        }


    }


}
