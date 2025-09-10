package org.silverstar.postcount.support;

import org.junit.jupiter.api.BeforeEach;
import org.silverstar.postcount.repository.entity.CouponWalletEntity;
import org.silverstar.postcount.repository.jpa.JpaCouponTransferHistoryRepository;
import org.silverstar.postcount.repository.jpa.JpaCouponWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SetupCouponTrasnferTemplate {

    @Autowired
    private JpaCouponWalletRepository jpaCouponWalletRepository;

    @Autowired
    protected JpaCouponTransferHistoryRepository jpaCouponTransferHistoryRepository;

    protected final Long COUPON_ID = 1L;
    protected final Long USER_A = 100L;
    protected final Long USER_B = 200L;

    @BeforeEach
    void setUp() {
        jpaCouponTransferHistoryRepository.deleteAll();
        jpaCouponWalletRepository.deleteAll();

        jpaCouponWalletRepository.save(new CouponWalletEntity(null, USER_A, COUPON_ID, 10L));
        jpaCouponWalletRepository.save(new CouponWalletEntity(null, USER_B, COUPON_ID, 10L));
    }
}