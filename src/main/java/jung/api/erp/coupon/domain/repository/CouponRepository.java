package jung.api.erp.coupon.domain.repository;

import jung.api.erp.coupon.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long>, CouponRepositoryCustom {

}
