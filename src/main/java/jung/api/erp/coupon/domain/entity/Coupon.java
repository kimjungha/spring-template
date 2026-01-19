package jung.api.erp.coupon.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jung.global.error.BusinessErrorCode;
import jung.global.exception.BusinessException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "available_count")
    private Long availableCount; //이용가능한 쿠폰 수

    @Column(name = "price")
    private BigDecimal price; //쿠폰 할인 가격

    @Override
    public String toString() {
        return "Coupon{" +
            "couponId=" + couponId +
            ", title='" + title + '\'' +
            ", availableCount=" + availableCount +
            '}';
    }

    // 쿠폰 차감
    public void decrease() {
        validateCouponCount();
        availableCount = availableCount-1;
    }

    // 쿠폰 수 검증
    private void validateCouponCount() {
        if(availableCount<1){
            throw new BusinessException(BusinessErrorCode.COUPON_NO_VALUE);
        }
    }
}
