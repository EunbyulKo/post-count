package org.silverstar.postcount.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_coupon_wallet",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","coupon_id"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CouponWalletEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="user_id", nullable=false)
    private Long userId;

    @Column(name="coupon_id", nullable=false)
    private Long couponId;

    @Column(nullable=false)
    @Setter
    private long amount;

}

