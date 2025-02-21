package jung.api.erp.coupon.service;

public interface CouponService{
    public void couponDecrease(Long couponId);
    public void couponDistributedDecrease(String lockName,Long couponId);
}
