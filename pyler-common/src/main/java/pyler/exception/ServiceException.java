package pyler.exception;

import pyler.enums.ErrorCode;

public class ServiceException extends CustomException{

    public ServiceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ServiceException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
