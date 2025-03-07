package pyler.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import pyler.domain.dto.ErrorResponse;
import pyler.domain.dto.FieldError;
import pyler.enums.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 전역 예외처리
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("Exception Error: {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Vaildation Error: {}", e.getMessage());

        List<FieldError> fieldErrors = getFieldErrors(e.getBindingResult());

        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getStatus())
                .body(ErrorResponse.of(ErrorCode.BAD_REQUEST, fieldErrors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected Error", e);

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        log.error("인증 예외 발생: {}", e.getMessage(), e);

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    // 파일 크기 초과 예외 처리
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(ErrorCode.IMAGE_SIZE_ERROR.getStatus())
                .body(ErrorResponse.of(ErrorCode.IMAGE_IS_NOT_EXIST));
    }

    // ServiceException 처리
//    @ExceptionHandler(ServiceException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException ex) {
//        return ResponseEntity.badRequest().body(ex.getMessage());
//    }

    /***
     * Filed Error 변환 유틸 메소트
     */
    private List<FieldError> getFieldErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> FieldError.builder()
                        .field(error.getField())
                        .value(error.getRejectedValue() != null ? error.getRejectedValue().toString() : "")
                        .reason(error.getDefaultMessage())
                        .build()
                ).collect(Collectors.toList());
    }

}
