package org.silverstar.postcount.service;

import org.junit.jupiter.api.Test;
import org.silverstar.postcount.repository.entity.CouponWalletEntity;
import org.silverstar.postcount.support.SetupCouponTrasnferTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.silverstar.postcount.support.ConcurrentTestTemplate.runTwoThreads;


class CouponTransferServiceTest extends SetupCouponTrasnferTemplate {

    @Autowired
    CouponTransferService couponTransferService;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Test
    void 성공_이체() {
        // when
        couponTransferService.transfer(USER_A, USER_B, COUPON_ID, 3);

        // then
        CouponWalletEntity a = couponTransferService.getUserWallet(USER_A, COUPON_ID);
        CouponWalletEntity b = couponTransferService.getUserWallet(USER_B, COUPON_ID);
        assertThat(a.getAmount()).isEqualTo(7L);
        assertThat(b.getAmount()).isEqualTo(13L);
    }

    @Test
    void 자기자신_이체() {
        couponTransferService.transfer(USER_A, USER_A, COUPON_ID, 5);

        CouponWalletEntity a = couponTransferService.getUserWallet(USER_A, COUPON_ID);
        assertThat(a.getAmount()).isEqualTo(10L);
    }

    @Test
    void 잔액부족_예외() {
        assertThatThrownBy(() -> couponTransferService.transfer(USER_A, USER_B, COUPON_ID, 999))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("insufficient");

        // then
        CouponWalletEntity a = couponTransferService.getUserWallet(USER_A, COUPON_ID);
        CouponWalletEntity b = couponTransferService.getUserWallet(USER_B, COUPON_ID);
        assertThat(a.getAmount()).isEqualTo(10L);
        assertThat(b.getAmount()).isEqualTo(10L);
    }

    @Test
    void 교차이체_동시실행_합계보존() throws Exception {

        runTwoThreads(transactionManager,
                () -> couponTransferService.transfer(USER_B, USER_A, COUPON_ID, 1),
                () -> couponTransferService.transfer(USER_A, USER_B, COUPON_ID, 1)
        );

        // then
        CouponWalletEntity a = couponTransferService.getUserWallet(USER_A, COUPON_ID);
        CouponWalletEntity b = couponTransferService.getUserWallet(USER_B, COUPON_ID);

        assertThat(a.getAmount()).isEqualTo(10L);
        assertThat(b.getAmount()).isEqualTo(10L);

        assertThat(a.getAmount() + b.getAmount()).isEqualTo(20L);

        assertThat(jpaCouponTransferHistoryRepository.count()).isEqualTo(2);
    }
}
