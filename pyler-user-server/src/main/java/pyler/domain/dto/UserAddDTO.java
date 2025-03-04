package pyler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddDTO {

    private String userName;
    private String userEmail;
    private String userPassword;
    private Boolean isMaster;
}
