package org.silverstar.postcount.repository.jpa;

import org.silverstar.postcount.repository.entity.CouponRemainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponRemainRepository extends JpaRepository<CouponRemainEntity, Long> {
}
