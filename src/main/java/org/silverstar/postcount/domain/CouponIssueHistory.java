package org.silverstar.postcount.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CouponIssueHistory {
    private Long id;
    private long couponId;
    private String userId;

    @Builder
    public CouponIssueHistory(Long id, long couponId, String userId) {
        this.id = id;
        this.couponId = couponId;
        this.userId = userId;
    }
}
