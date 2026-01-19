package jung.global.exception;

import jung.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    // Builder 는 선택의 자유를 주기위한 패턴, ErrorCode 처럼 항상 동일하게 들어간다면 new
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage(),
                LocalDateTime.now()
        );
    }
}
