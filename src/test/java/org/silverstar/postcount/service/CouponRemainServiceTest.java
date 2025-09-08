package org.silverstar.postcount.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.silverstar.postcount.support.SetupCouponTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.silverstar.postcount.support.ConcurrentTestTemplate.run;

class CouponRemainServiceTest extends SetupCouponTemplate {

    @Autowired
    private CouponRemainService couponRemainService;

    @Test
    void getCouponRemaining_success() {
        int remaining = couponRemainService.getCouponRemaining(couponId);
        Assertions.assertEquals(10, remaining);
    }

    @Test
    void getCouponRemaining_fail() {
        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> couponRemainService.getCouponRemaining(2L));
    }

    @Test
    void decreaseCouponRemain_concurrentRequests() throws Exception {
        int threads = 100;

        List<Future<?>> futures = run(threads, 20, () -> couponRemainService.decreaseCouponRemain(couponId));

        //then
        long finalCount = couponRemainService.getCouponRemaining(couponId);
        Assertions.assertEquals(0, finalCount);

        assertThatThrownBy(() -> {
            for (Future<?> f : futures) {
                try { f.get(); }
                catch (ExecutionException e) {
                    throw e.getCause();
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new AssertionError(e);
                }
            }
        }).satisfiesAnyOf(
            ex -> assertThat(ex).hasRootCauseInstanceOf(org.springframework.orm.ObjectOptimisticLockingFailureException.class),
            ex -> assertThat(ex).hasRootCauseInstanceOf(org.hibernate.StaleObjectStateException.class)
        );

    }
}