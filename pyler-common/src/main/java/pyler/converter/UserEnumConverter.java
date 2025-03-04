package pyler.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pyler.enums.ErrorCode;
import pyler.enums.UserEnum;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserEnumConverter implements AttributeConverter<UserEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserEnum userEnum) {
        if (userEnum == null) {
            return null;
        }
        return userEnum.getCode();
    }

    @Override
    public UserEnum convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return Stream.of(UserEnum.values())
                .filter(e -> e.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Role Code: " + code));
    }
}
