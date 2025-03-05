package pyler.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "P-400", "잘못된 요청"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "P-404", "리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "P-405", "지원하지 않은 HTTP 메소드 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "P-500", "서버 오류"),

    ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "P-400", "사용중인 사용자 입니다."),
    USER_ADD_SUCCESS(HttpStatus.OK, "P-200", "사용자 등록 완료"),
    USER_LOGIN_SUCCESS(HttpStatus.OK, "P-200", "Login Success"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "P-404", "등록된 사용자가 아닙니다."),
    INVALID_USER_PASSWORD(HttpStatus.BAD_REQUEST, "P-400", "잘못된 비밀번호 입니다."),
    EMAIL_OR_PASSWORD_IS_NOT_EXIST(HttpStatus.BAD_REQUEST, "P-400", "email과 password는 필수 입니다."),
    INVALIDE_ROLE_CODE(HttpStatus.NOT_FOUND, "{-404", "잘못된 User 권한 코드입니다."),
    ALREADY_EXIST_CATEGORY(HttpStatus.BAD_REQUEST, "P-400", "존재하는 카테고리입니다."),
    CATGORY_IS_NOT_MATCHING(HttpStatus.BAD_REQUEST, "P-400", "카테고리는 PERSON, LANDSCAPE, ANIMAL, FOOD, OTHERS 만 등록 가능합니다."),
    IMAGE_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "P-404", "해당 카테고리가 등록된 이미지가 없습니다."),
    IMAGE_SIZE_ERROR(HttpStatus.BAD_REQUEST, "P-400", "업로드 용량 제한은 40MB이하입니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "P-400", "IMAGE 파일만 업로드 가능 합니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "P-401", "인증이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "P-403", "접근 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "P-411", "유요하지 않은 토큰입니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;


}
