package org.silverstar.postcount.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.silverstar.postcount.domain.Coupon;

@Entity
@Table(name = "tb_coupon")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    public CouponEntity(Coupon coupon) {
        this.id = coupon.getId();
        this.postId = coupon.getPostId();
    }

    public Coupon toCoupon() {
        return Coupon.builder()
                .id(id)
                .postId(postId)
                .build();
    }

}
