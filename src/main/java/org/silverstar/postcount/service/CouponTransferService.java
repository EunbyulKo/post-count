package org.silverstar.postcount.service;

import lombok.RequiredArgsConstructor;
import org.silverstar.postcount.repository.entity.CouponTransferHistoryEntity;
import org.silverstar.postcount.repository.entity.CouponWalletEntity;
import org.silverstar.postcount.repository.jpa.JpaCouponTransferHistoryRepository;
import org.silverstar.postcount.repository.jpa.JpaCouponWalletRepository;
import org.silverstar.postcount.util.LockKey;
import org.silverstar.postcount.util.LockOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CouponTransferService {

    private final JpaCouponWalletRepository jpaCouponWalletRepository;
    private final JpaCouponTransferHistoryRepository jpaCouponTransferHistoryRepository;

    @Transactional(readOnly = true)
    public CouponWalletEntity getUserWallet(Long userId, Long couponId) {
        return jpaCouponWalletRepository.findByUserIdAndCouponId(userId, couponId).orElse(null);
    }

    @Transactional(timeout = 10) // 데드락/지연 대비 타임아웃
    public void transfer(Long fromUser, Long toUser, Long couponId, long amount) {
        // TODO : DTO validate 만들어 이동
        if (fromUser == null || toUser == null) throw new IllegalArgumentException("fromUser and toUser cannot be null");
        if (couponId == null) throw new IllegalArgumentException("couponId cannot be null");
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
        if (fromUser.equals(toUser)) {
            return;
        }
        // end

        // 1) LockKey 생성 및 Lock 순서 지정
        LockKey fromUserLock = new LockKey(fromUser, couponId);
        LockKey toUserLock = new LockKey(toUser, couponId);
        List<LockKey> keys = LockOrder.order(fromUserLock,toUserLock);

        // 2) 순서대로 잠금 획득
        Map<LockKey, CouponWalletEntity> locked = new HashMap<>();
        for (LockKey k : keys) {
            CouponWalletEntity w = jpaCouponWalletRepository.lockByUserAndCoupon(k.userId(), k.couponId())
                    .orElseThrow(() -> new IllegalArgumentException("wallet not found: " + k));
            locked.put(k, w);
        }

        // 3) 쿠폰 이동
        CouponWalletEntity from = locked.get(fromUserLock);
        CouponWalletEntity to   = locked.get(toUserLock);

        // TODO : domain 만든 후 domain으로 이동
        if (from.getAmount() < amount) throw new IllegalStateException("insufficient");
        // end

        from.setAmount(from.getAmount() - amount);
        to.setAmount(to.getAmount() + amount);

        // 4) 이력 기록
        jpaCouponTransferHistoryRepository.save(new CouponTransferHistoryEntity(
                null, fromUser, toUser, couponId, amount
        ));
    }

}
