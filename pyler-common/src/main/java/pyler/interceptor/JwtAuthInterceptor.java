package pyler.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import pyler.client.JwtTokenClient;
import pyler.domain.dto.TokenInfoDTO;
import pyler.enums.ErrorCode;
import pyler.exception.AuthException;

/**
 * Client token Interceptor
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "jwt.client.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtTokenClient jwtTokenClient;

    private static final String authorizationHeader = "Authorization";
    private static final String bearberPrfix= "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("JwtAuthInterceptor preHandle for path: {}", request.getRequestURI());

        String jwt = resolveToken((request)); //Client Header 내의 JWt Token

        if(StringUtils.hasText(jwt)) {
            try {
                //Token 검증(pyler-jwt-server)
                boolean isVal = jwtTokenClient.validationToken(jwt);

                if(!isVal){
                    log.error("유효하지 않은 토큰: {}", jwt);
                    throw new AuthException(ErrorCode.INVALID_TOKEN);
                }

                TokenInfoDTO tokenInfoDTO = jwtTokenClient.tokenInfo(jwt); //token 정보

                request.setAttribute("userId", tokenInfoDTO.getUserId());
                request.setAttribute("userEmail", tokenInfoDTO.getUserEmail());
                request.setAttribute("isMaster", tokenInfoDTO.getIsMaster());

                log.debug("인증 Success - user: {}", tokenInfoDTO.getUserEmail());

                return true;

            } catch (Exception e) {
                log.error("Token 검증 Error: {}", e.getMessage());
                throw new AuthException(ErrorCode.INVALID_TOKEN);
            }
        }
        log.error("Token Header Error");
        throw new AuthException(ErrorCode.UNAUTHORIZED);
    }

    /**
     * Client Header에서 Token 추출
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request) {
        String bearberToken = request.getHeader(authorizationHeader);
        if (StringUtils.hasText(bearberToken) && bearberToken.startsWith(bearberPrfix)) {
            return bearberToken.substring(7);
        }

        return null;
    }
}
