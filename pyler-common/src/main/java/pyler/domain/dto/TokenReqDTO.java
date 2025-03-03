package pyler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token API 통신용 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenReqDTO {
    private String accessToken;
    private String refreshToken;
}
