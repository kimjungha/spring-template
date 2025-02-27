package jung.api.erp.coupon.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static jung.api.erp.coupon.domain.entity.QCoupon.coupon;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    /**
     * 쿠폰 조회
     * 동적 조건 : 쿠폰 갯수, 쿠폰 이름, 쿠폰 할인 가격
     */

    @Override
    public Long findCoupons(int num) {

        //동적 조건을 위해 BooleanBuilder 사용
        BooleanBuilder condition = new BooleanBuilder();

        //보통 리퀘스트에 따라서 나누면 될듯
        if(num == 1){
            this.isCountOver(condition, 30);
        }else{
            this.isEqualPrice(condition, new BigDecimal(1000));
        }

        this.isEqualName(condition, "SALE_1000");

        return queryFactory.select(coupon.count())
            .from(coupon)
            .where(condition)
            .fetchOne();

    }

    private void isEqualPrice(BooleanBuilder builder, BigDecimal price) {
        builder.and(coupon.price.eq(price));
    }

    private void isEqualName(BooleanBuilder builder, String couponName) {
        builder.and(coupon.title.eq(couponName));
    }

    // 동적 조건 재사용을 위해 메서드로 생성
    private void isCountOver(BooleanBuilder builder, int count) {
        builder.and(coupon.availableCount.goe(count));
    }


}
