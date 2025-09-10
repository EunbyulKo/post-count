package org.silverstar.postcount.repository.jpa;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.silverstar.postcount.repository.entity.CouponWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaCouponWalletRepository extends JpaRepository<CouponWalletEntity, Long> {

    Optional<CouponWalletEntity> findByUserIdAndCouponId(@Param("userId") Long userId,
                                                         @Param("couponId") Long couponId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select w from CouponWalletEntity w
        where w.userId = :userId and w.couponId = :couponId
    """)
    @QueryHints({
        @QueryHint(name = "jakarta.persistence.lock.timeout", value = "5000")
    })
    Optional<CouponWalletEntity> lockByUserAndCoupon(@Param("userId") Long userId,
                                                     @Param("couponId") Long couponId);
}
