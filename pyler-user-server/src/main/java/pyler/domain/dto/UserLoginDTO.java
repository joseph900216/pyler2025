package pyler.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginDTO {

    private String email;
    private String password;
}
