package jung.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum BusinessErrorCode implements ErrorCode {

    // book api
    BOOK_NO_VALUE(HttpStatus.BAD_REQUEST, "B001", "찾는 책이 없습니다."),

    // coupon api
    COUPON_NO_VALUE(HttpStatus.BAD_REQUEST, "C001", "쿠폰이 소진되었습니다."),

    LOGIN_FAILURE(HttpStatus.BAD_REQUEST, "A001", "로그인 실패하셨습니다."),
    LOGIN_BAD_REQUEST(HttpStatus.BAD_REQUEST, "A002", "로그인 이메일 혹은 패스워드가 일치하지 않습니다."),
    LOGIN_RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "A004", "로그인 시도 횟수를 초과하였습니다. 10분 후 다시 시도해주세요."),

    // goods api
    GOOD_NO_VALUE(HttpStatus.BAD_REQUEST, "G001", "찾는 상품이 없습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
