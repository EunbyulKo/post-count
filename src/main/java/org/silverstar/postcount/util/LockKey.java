package org.silverstar.postcount.util;

public record LockKey(Long userId, Long couponId) implements Comparable<LockKey> {
    public LockKey {
        if (userId == null || couponId == null) throw new IllegalArgumentException("null key");
    }

    @Override
    public int compareTo(LockKey o) {
        int c = userId.compareTo(o.userId);
        return (c != 0) ? c : couponId.compareTo(o.couponId);
    }
}

