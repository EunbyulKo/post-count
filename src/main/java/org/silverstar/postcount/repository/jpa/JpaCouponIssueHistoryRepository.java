package org.silverstar.postcount.repository.jpa;

import org.silverstar.postcount.repository.entity.CouponIssueHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponIssueHistoryRepository extends JpaRepository<CouponIssueHistoryEntity, Long> {}
