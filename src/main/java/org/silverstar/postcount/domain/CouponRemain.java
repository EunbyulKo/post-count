package org.silverstar.postcount.domain;

import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CouponRemain {
    private long couponId;
    private int remaining;

    @Version
    private long version;

    @Builder
    public CouponRemain(Long couponId, int remaining) {
        if (couponId == null) {
            throw new NullPointerException("couponId is null");
        }

        this.couponId = couponId;
        this.remaining = remaining;
    }
    public void decrementRemaining() {
        if (this.remaining > 0) {
            this.remaining--;
        }
    }
}
