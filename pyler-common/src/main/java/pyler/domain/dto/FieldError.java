package pyler.domain.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class FieldError {
    private String field;
    private String value;
    private String reason;

    public FieldError(String field, String value, String reason) {
        this.field = field;
        this.value = value;
        this.reason = reason;
    }

    public static List<FieldError> of(BindingResult bindingResult) {
        List<FieldError> fieldErrors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            FieldError fieldErrorDTO = new FieldError(
                    fieldError.getField(),
                    fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                    fieldError.getDefaultMessage()
            );
            fieldErrors.add(fieldErrorDTO);
        });
        return fieldErrors;
    }
}
