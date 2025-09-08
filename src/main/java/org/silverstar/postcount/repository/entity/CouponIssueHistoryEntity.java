package org.silverstar.postcount.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.silverstar.postcount.domain.Coupon;
import org.silverstar.postcount.domain.CouponIssueHistory;

@Entity
@Table(name = "tb_coupon_issue_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CouponIssueHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    public CouponIssueHistoryEntity(CouponIssueHistory couponIssueHistory) {
        this.id = couponIssueHistory.getId();
        this.couponId = couponIssueHistory.getCouponId();
        this.userId = couponIssueHistory.getUserId();
    }

    public CouponIssueHistory toCouponIssueHistory() {
        return CouponIssueHistory.builder()
                .id(id)
                .couponId(couponId)
                .userId(userId)
                .build();
    }

}
