package org.silverstar.postcount.repository;

import lombok.RequiredArgsConstructor;
import org.silverstar.postcount.domain.CouponRemain;
import org.silverstar.postcount.repository.entity.CouponRemainEntity;
import org.silverstar.postcount.repository.jpa.JpaCouponRemainRepository;
import org.silverstar.postcount.service.interfaces.CouponRemainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CouponRemainRepositoryImpl implements CouponRemainRepository {

    private final JpaCouponRemainRepository jpaCouponRemainRepository;

    @Override
    public CouponRemain findById(Long couponId) {
        CouponRemainEntity entity = jpaCouponRemainRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("coupon-remain not found"));
        return entity.toCouponRemain();
    }

    @Override
    @Transactional
    public CouponRemain create(CouponRemain couponRemain) {
        CouponRemainEntity CouponRemainEntity = jpaCouponRemainRepository.save(new CouponRemainEntity(couponRemain));
        return CouponRemainEntity.toCouponRemain();
    }

    @Override
    public CouponRemain update(CouponRemain couponRemain) {
        CouponRemainEntity entity = jpaCouponRemainRepository.findById(couponRemain.getCouponId()).orElseThrow();

        if (entity.getRemaining() <= 0) {
            throw new IllegalStateException("insufficient");
        }

        entity.setRemaining(couponRemain.getRemaining());

        return entity.toCouponRemain(); // version 체크 후 에러
    }

}
