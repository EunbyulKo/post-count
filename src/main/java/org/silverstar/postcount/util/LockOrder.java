package org.silverstar.postcount.util;

import java.util.List;

public final class LockOrder {

    private LockOrder() {}

    public static List<LockKey> order(LockKey a, LockKey b) {
        if (a.equals(b)) return List.of(a);
        return (a.compareTo(b) <= 0) ? List.of(a, b) : List.of(b, a);
    }

}

