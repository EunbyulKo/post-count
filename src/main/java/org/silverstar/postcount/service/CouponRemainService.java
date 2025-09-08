package org.silverstar.postcount.service;

import lombok.RequiredArgsConstructor;
import org.silverstar.postcount.domain.CouponRemain;
import org.silverstar.postcount.service.interfaces.CouponRemainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponRemainService {

    private final CouponRemainRepository couponRemainRepository;

    @Transactional(readOnly = true)
    public int getCouponRemaining(Long id) {
        CouponRemain couponRemain = couponRemainRepository.findById(id);

        return couponRemain.getRemaining();
    }

    @Transactional
    public long decreaseCouponRemain(Long id) {
        CouponRemain couponRemain = couponRemainRepository.findById(id);
        couponRemain.decrementRemaining();
        couponRemainRepository.update(couponRemain);
        return couponRemain.getRemaining();
    }

}
