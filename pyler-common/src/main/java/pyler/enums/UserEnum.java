package pyler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserEnum {

    ADMIN(1, "admin"),
    USER(2, "user");

    private int code;
    private String name;

}
