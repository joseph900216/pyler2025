package pyler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserEnum {

    IMAGE_UPLOAD(1, "UPLOAD"),
    IMAGE_READ(2, "READ"),
    IMAGE_UPDATE(3, "UPDATE"),
    IMAGE_DELETE(4, "DELETE");


    private final int code;
    private final String name;

}
