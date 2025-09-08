package org.silverstar.postcount.repository.jpa;

import org.silverstar.postcount.repository.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponRepository extends JpaRepository<CouponEntity, Long> {

}
