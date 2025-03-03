package pyler.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public class UserDTO {

    private String email;
    private String passWord;

}
