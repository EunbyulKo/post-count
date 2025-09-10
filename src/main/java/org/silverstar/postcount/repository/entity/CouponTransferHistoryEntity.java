package org.silverstar.postcount.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_coupon_transfer_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CouponTransferHistoryEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Long fromUserId;
    private Long toUserId;
    private Long couponId;
    private long amount;

}
