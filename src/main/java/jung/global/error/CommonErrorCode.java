package jung.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    // auth
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "400", "리프레시 토큰이 유효하지 않습니다"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "401", "액세스 토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "401", "유효하지 않은 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "401", "지원하지 않는 토큰 형식입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "403", "접근 권한이 없습니다."),

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "400", "요청값이 잘못되었습니다."),
    INVALID_STATUS_VALUE(HttpStatus.BAD_REQUEST, "401", "요청한 상태값이 잘못되었습니다."),
    INVALID_NOT_VALID(HttpStatus.BAD_REQUEST, "402", "요청값 검증에 실패하였습니다."),
    DUPLICATE_NOT(HttpStatus.BAD_REQUEST, "C005", "멱등성 키 중복 요청입니다."),


    JSON_CONVERT(HttpStatus.BAD_REQUEST, "402", "JSON 변환이 되지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
