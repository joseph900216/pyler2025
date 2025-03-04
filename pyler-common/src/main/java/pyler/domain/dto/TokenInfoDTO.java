package pyler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token 검증 정보 DTO(JWT -> Client)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfoDTO {
    private Long userId;
    private String userEmail;
    private Boolean isMaster;
}
