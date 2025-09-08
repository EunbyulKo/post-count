package org.silverstar.postcount.service;

import lombok.RequiredArgsConstructor;
import org.silverstar.postcount.domain.CouponIssueHistory;
import org.silverstar.postcount.service.interfaces.CouponIssueHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponIssueHistoryRepository couponIssueHistoryRepository;

    private final CouponRemainService couponRemainService;

    @Transactional
    public long allocateCoupon(Long couponId, String userId) {

        // 잔여 쿠폰 반영
        long remaining = couponRemainService.decreaseCouponRemain(couponId);

        // 쿠폰 할당 히스토리 저장
        CouponIssueHistory couponIssueHistory = CouponIssueHistory.builder()
                .couponId(couponId)
                .userId(userId)
                .build();
        couponIssueHistoryRepository.save(couponIssueHistory);

        return remaining;
    }

}
