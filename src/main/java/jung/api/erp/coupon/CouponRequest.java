package jung.api.erp.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
@Getter
@RequiredArgsConstructor
public class CouponRequest {

    private final String title;
    private final long count;
    private final BigDecimal price;
}
