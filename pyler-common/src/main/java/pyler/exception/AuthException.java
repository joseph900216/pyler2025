package pyler.exception;

import pyler.enums.ErrorCode;

public class AuthException extends CustomException{

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
