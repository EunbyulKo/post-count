package org.silverstar.postcount.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.silverstar.postcount.domain.CouponRemain;

@Entity
@Table(name = "tb_coupon_remain")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CouponRemainEntity {

    @Id
    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "remaining", nullable = false)
    @Setter
    private int remaining = 0;

    @Version
    private long version;

    public CouponRemainEntity(CouponRemain couponRemain) {
        this.couponId = couponRemain.getCouponId();
        this.remaining = couponRemain.getRemaining();
    }

    public CouponRemain toCouponRemain() {
        return CouponRemain.builder()
                .couponId(couponId)
                .remaining(remaining)
                .build();
    }

}
