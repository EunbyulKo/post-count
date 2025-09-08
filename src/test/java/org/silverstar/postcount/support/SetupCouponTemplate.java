package org.silverstar.postcount.support;

import org.junit.jupiter.api.BeforeEach;
import org.silverstar.postcount.domain.CouponRemain;
import org.silverstar.postcount.domain.Post;
import org.silverstar.postcount.domain.PostStat;
import org.silverstar.postcount.repository.jpa.JpaCouponRemainRepository;
import org.silverstar.postcount.repository.jpa.JpaPostRepository;
import org.silverstar.postcount.repository.jpa.JpaPostStatRepository;
import org.silverstar.postcount.service.interfaces.CouponRemainRepository;
import org.silverstar.postcount.service.interfaces.PostRepository;
import org.silverstar.postcount.service.interfaces.PostStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SetupCouponTemplate {

    @Autowired
    private CouponRemainRepository couponRemainRepository;

    @Autowired
    private JpaCouponRemainRepository jpaCouponRemainRepository;

    protected Long couponId = 1L;
    protected int remaining = 10;

    @BeforeEach
    @Transactional
    void setUp() {
        jpaCouponRemainRepository.deleteAllInBatch();

        CouponRemain couponRemain = CouponRemain.builder()
                .couponId(couponId)
                .remaining(remaining)
                .build();
        couponRemainRepository.create(couponRemain);

    }
}