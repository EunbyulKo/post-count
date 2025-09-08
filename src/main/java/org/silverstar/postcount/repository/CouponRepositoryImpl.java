package org.silverstar.postcount.repository;

import lombok.RequiredArgsConstructor;
import org.silverstar.postcount.domain.Coupon;
import org.silverstar.postcount.repository.entity.CouponEntity;
import org.silverstar.postcount.repository.jpa.JpaCouponRepository;
import org.silverstar.postcount.service.interfaces.CouponRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final JpaCouponRepository jpaCouponRepository;

    @Override
    public Coupon findById(Long CouponId) {
        CouponEntity entity = jpaCouponRepository.findById(CouponId)
                .orElseThrow(() -> new IllegalArgumentException("coupon not found"));
        return entity.toCoupon();
    }

    @Override
    public Coupon save(Coupon Coupon) {
        CouponEntity entity = jpaCouponRepository.save(new CouponEntity(Coupon));
        return entity.toCoupon();
    }

}
