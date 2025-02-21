package jung.api.erp.coupon.service;

import jung.api.erp.coupon.domain.entity.Coupon;
import jung.api.erp.coupon.domain.entity.CouponRepository;
import jung.global.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;

    @Transactional
    public void couponDecrease(Long couponId){
       couponRepository.findById(couponId)
           .ifPresent(Coupon::decrease); // Todo : 쿠폰이 없다면 보완 필요
    }

    @DistributedLock(key = "#lockName")
    public void couponDistributedDecrease(String lockName, Long couponId){
        couponRepository.findById(couponId)
            .ifPresent(Coupon::decrease);
    }


}
