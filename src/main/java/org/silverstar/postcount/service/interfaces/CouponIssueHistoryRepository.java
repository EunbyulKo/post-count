package org.silverstar.postcount.service.interfaces;

import org.silverstar.postcount.domain.CouponIssueHistory;

public interface CouponIssueHistoryRepository {
    CouponIssueHistory save(CouponIssueHistory couponIssueHistory);
}
