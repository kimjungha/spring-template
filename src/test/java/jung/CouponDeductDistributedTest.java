package jung;

import jung.api.erp.coupon.domain.entity.Coupon;
import jung.api.erp.coupon.domain.repository.CouponRepository;
import jung.api.erp.coupon.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class CouponDeductDistributedTest {
    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    private Coupon coupon;

    @BeforeEach
    void setUp(){
        coupon = Coupon.builder()
            .title("신규가입 3000원 할인")
            .availableCount(100L)
            .build();
        couponRepository.save(coupon);
    }

    /**
     * Feature: 쿠폰 차감 동시성 테스트
     * 멀티스레드를 이용하여 동시성 테스트 수행
     */
    @Test
    void 쿠폰차감_분산락_적용_동시성100명_테스트() throws InterruptedException {
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads); //멀티스레드 관리하는 스레드 풀 생성
        CountDownLatch latch = new CountDownLatch(numberOfThreads); //초기 카운트 n 으로 설정

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    // 분산락 적용 메서드 호출 (락의 key는 쿠폰의 name으로 설정)
                    couponService.couponDistributedDecrease("coupon", coupon.getCouponId());
                } finally {
                    latch.countDown(); // 작업 끝나면 카운트 감소
                }
            });
        }

        latch.await(); // 모든 스레드 종료될 때까지 기다림

        Coupon persistCoupon = couponRepository.findById(coupon.getCouponId())
            .orElseThrow(IllegalArgumentException::new);

        assertThat(persistCoupon.getAvailableCount()).isZero();
        System.out.println("잔여 쿠폰 개수 = " + persistCoupon.getAvailableCount());
    }

    /**
     * Feature: 쿠폰 차감 동시성 테스트
     *  분산락이 적용되지 않은 경우 동시 쿠폰 차감을 요청할 때
     */
//    @Test
//    void 쿠폰차감_분산락_미적용_동시성100명_테스트() throws InterruptedException {
//        int numberOfThreads = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads); //멀티스레드 관리하는 스레드 풀 생성
//        CountDownLatch latch = new CountDownLatch(numberOfThreads); //초기 카운트 n 으로 설정
//
//        for (int i = 0; i < numberOfThreads; i++) {
//            executorService.submit(() -> {
//                try {
//                    // 분산락 적용 메서드 호출 (락의 key는 쿠폰의 name으로 설정)
//                    couponService.couponDecrease( coupon.getCouponId());
//                } finally {
//                    latch.countDown(); // 작업 끝나면 카운트 감소
//                }
//            });
//        }
//
//        latch.await(); // 모든 스레드 종료될 때까지 기다림
//
//        Coupon persistCoupon = couponRepository.findById(coupon.getCouponId())
//            .orElseThrow(IllegalArgumentException::new);
//
//        assertThat(persistCoupon.getAvailableCount()).isZero();
//        System.out.println("잔여 쿠폰 개수 = " + persistCoupon.getAvailableCount());
//    }
}
