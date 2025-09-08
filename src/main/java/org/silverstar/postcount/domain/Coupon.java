package org.silverstar.postcount.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * 시나리오
 * - 포스트 1개당 10개의 할인쿠폰 선착순 증정
 */
@Getter
public class Coupon {
    private Long id;
    private long postId;

    @Builder
    public Coupon(Long id, long postId) {
        this.id = id;
        this.postId = postId;
    }
}
