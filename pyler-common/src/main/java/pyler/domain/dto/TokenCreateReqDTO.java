package pyler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token 생성 요청용 DTO(Client -> JWT)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenCreateReqDTO {
    private Long userId;
    private String userEmail;
    private Integer userRoel;
}
