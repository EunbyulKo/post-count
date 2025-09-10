package org.silverstar.postcount.repository.jpa;

import org.silverstar.postcount.repository.entity.CouponTransferHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponTransferHistoryRepository extends JpaRepository<CouponTransferHistoryEntity, Long> {
}
