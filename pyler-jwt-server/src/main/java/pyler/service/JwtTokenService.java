package pyler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pyler.domain.dto.TokenDTO;
import pyler.domain.dto.TokenInfoDTO;

/**
 * token controller에서 사용할 인터페이스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenCreateService jwtTokenCreateService;
    private final JwtTokenValidationService jwtTokenValidationService;
    private final JwtTokenExtractionService jwtTokenExtractionService;
    private final JwtTokenManageService jwtTokenManageService;

    /**
     * Token 생성
     * @param userId
     * @param userEmail
     * @param userRole
     * @return
     */
    public TokenDTO createToken(Long userId, String userEmail, int userRole) {
        return jwtTokenCreateService.createToken(userId, userEmail, userRole);
    }

    /**
     * token 검증
     * @param token
     * @return
     */
    public boolean validationToken(String token) {
        return jwtTokenValidationService.validatetoken(token);
    }

    /**
     * token 정보 조회
     * @param token
     * @return
     */
    public TokenInfoDTO getTokenInfo(String token) {
        Long userId = jwtTokenExtractionService.getUserId(token);
        String userEmail = jwtTokenExtractionService.getUserEmail(token);
        Integer userRole = jwtTokenExtractionService.getUserRole(token);

        return TokenInfoDTO.builder()
                .userId(userId)
                .userEmail(userEmail)
                .userRole(userRole)
                .build();
    }

    /**
     * token 갱신
     * @param refreshToken
     * @return
     */
    public TokenDTO refreshAccessToken(String refreshToken) {
        return jwtTokenManageService.refreshAccessToken(refreshToken);
    }

    /**
     * token 무효화
     * @param accessToken
     */
    public void invalidationToken(String accessToken) {
        jwtTokenManageService.invalidationToken(accessToken);
    }

    /**
     * token 내, userId 조회
     * @param token
     * @return
     */
    public Long getUserID(String token) {
        return jwtTokenExtractionService.getUserId(token);
    }

    /**
     * token 내, userEmail 조회
     * @param token
     * @return
     */
    public String getUserEmail(String token) {
        return jwtTokenExtractionService.getUserEmail(token);
    }

    /**
     * token 내, userRole 조회
     * @param token
     * @return
     */
    public Integer getUserRole(String token) {
        return jwtTokenExtractionService.getUserRole(token);
    }

}
