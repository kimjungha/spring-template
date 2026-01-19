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

    // goods api
    GOOD_NO_VALUE(HttpStatus.BAD_REQUEST, "G001", "찾는 상품이 없습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
