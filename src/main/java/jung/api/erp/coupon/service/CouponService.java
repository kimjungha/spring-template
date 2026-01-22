package jung.api.erp.coupon.service;

public interface CouponService{
    void couponDecrease(Long couponId);
    void couponDistributedDecrease(String lockName,Long couponId);
}
