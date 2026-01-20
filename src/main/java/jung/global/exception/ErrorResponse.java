package jung.global.exception;

import jung.global.error.ErrorCode;

import java.time.LocalDateTime;

public record ErrorResponse(String code, String message, LocalDateTime timestamp) {

    // Builder 는 선택의 자유를 주기위한 패턴, ErrorCode 처럼 항상 동일하게 들어간다면 new
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage(),
                LocalDateTime.now()
        );
    }
}
