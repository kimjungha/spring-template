package jung;

import jung.api.erp.coupon.domain.entity.Coupon;
import jung.api.erp.coupon.domain.repository.CouponRepository;
import jung.global.config.database.JPAConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

@SpringBootTest
@Import({JPAConfig.class})
public class CouponTest {
    @Autowired
    private CouponRepository couponRepository;

    private Coupon coupon;

    @BeforeEach
    void setUp(){
        coupon = Coupon.builder()
            .title("SALE_1000")
            .availableCount(100L)
            .price(BigDecimal.valueOf(1000))
            .build();
        couponRepository.save(coupon);
    }
    @Test
    void 동적조건_쿠폰조회() throws InterruptedException {
        long countCnt = couponRepository.findCoupons(1);
        System.out.println("잔여 쿠폰 title = " + countCnt);

        long countCnt2 = couponRepository.findCoupons(2);
        System.out.println("잔여 쿠폰 title = " + countCnt2);
    }
}
