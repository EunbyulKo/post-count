package org.silverstar.postcount.service.interfaces;

import org.silverstar.postcount.domain.CouponRemain;

public interface CouponRemainRepository {
    CouponRemain findById(Long couponId);
    CouponRemain create(CouponRemain couponRemain);
    CouponRemain update(CouponRemain couponRemain);
}
