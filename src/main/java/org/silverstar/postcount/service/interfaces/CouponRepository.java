package org.silverstar.postcount.service.interfaces;

import org.silverstar.postcount.domain.Coupon;

public interface CouponRepository {
    Coupon findById(Long id);
    Coupon save(Coupon coupon);
}
