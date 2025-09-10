package org.silverstar.postcount.service;

import lombok.RequiredArgsConstructor;
import org.silverstar.postcount.service.interfaces.PostStatRepository;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PostRealtimeViewFlushJob {

    private final StringRedisTemplate redis;
    private final PostStatRepository postStatRepository;

    @Scheduled(fixedDelay = 2000L)
    @Transactional
    public void flush() {
        Set<String> keys = redis.keys("post:*:views");
        if (keys.isEmpty()) return;

        Map<Long, Long> deltas = new HashMap<>();
        for (String key : keys) {
            Long delta = getAndDelete(key);
            if (delta != null && delta > 0) {
                Long postId = Long.parseLong(key.split(":")[1]);
                deltas.merge(postId, delta, Long::sum);
            }
        }

        deltas.forEach(postStatRepository::incrementView);
    }

    private Long getAndDelete(String key) {
        return redis.execute((RedisCallback<Long>) con -> {
            byte[] raw = con.stringCommands().getDel(key.getBytes());
            if (raw == null) return 0L;
            return Long.parseLong(new String(raw));
        });
    }
}
