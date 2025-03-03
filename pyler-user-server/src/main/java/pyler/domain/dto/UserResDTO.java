package pyler.domain.dto;

import lombok.*;

//@Data
//@NoArgsConstructor
@Builder
@Getter
@AllArgsConstructor
public class UserResDTO {

    private long id;
    private String name;
    private String email;
    private int role;
    private String accessToken;
    private String refreshToken;

//    @Builder
//    public UserResDTO(long id, String name, String email, int role, String accessToken, String refreshToken) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.role = role;
//        this.accessToken = accessToken;
//        this.refreshToken = refreshToken;
//    }
}
