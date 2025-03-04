package pyler.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pyler.contents.RequestMap;
import pyler.domain.dto.*;
import pyler.service.JwtTokenService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMap.api + RequestMap.v1 + RequestMap.token)
public class JwtTokenController {

    private final JwtTokenService jwtTokenService;

    /**
     * Token 생성 API
     * @param tokenCreateReqDTO (userId, userEmail, isMaster)
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TokenDTO>> postCreateToken(@RequestBody TokenCreateReqDTO tokenCreateReqDTO) {
        TokenDTO tokenDTO = jwtTokenService.createToken(
                tokenCreateReqDTO.getUserId(),
                tokenCreateReqDTO.getUserEmail(),
                tokenCreateReqDTO.getIsMaster()
        );
        return ResEntity.success(tokenDTO);
    }

    /**
     * Token 유효성 검증 API
     * @param token
     * @return
     */
    @GetMapping("/validation")
    public ResponseEntity<ApiResponse<Boolean>> getValidationToken(@RequestParam String token) {
        boolean isValid = jwtTokenService.validationToken(token);
        return ResEntity.success(isValid);
    }

    /**
     * Token 갱신
     * @param tokenReqDTO
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenDTO>> postRefreshToken(@RequestBody TokenReqDTO tokenReqDTO) {
        TokenDTO tokenDTO = jwtTokenService.refreshAccessToken(tokenReqDTO.getRefreshToken());
        return ResEntity.success(tokenDTO);
    }

    /**
     * Token 정보 조회
     * @param token
     * @return
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<TokenInfoDTO>> getTokenInfo(@RequestParam String token) {
        TokenInfoDTO tokenInfoDTO = jwtTokenService.getTokenInfo(token);
        return ResEntity.success(tokenInfoDTO);
    }

    /**
     * Token 무효화
     * @param tokenReqDTO
     * @return
     */
    @PostMapping("/invalidate")
    public ResponseEntity<ApiResponse<Void>> postInvalidationToken(@RequestBody TokenReqDTO tokenReqDTO) {
        jwtTokenService.invalidationToken(tokenReqDTO.getAccessToken());
        return ResEntity.success("Token 무효화 Success", null);
    }

}
