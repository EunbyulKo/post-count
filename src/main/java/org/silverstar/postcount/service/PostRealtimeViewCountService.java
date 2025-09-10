package org.silverstar.postcount.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostRealtimeViewCountService {

    private final StringRedisTemplate redisTemplate;

    public long incrementView(Long postId) {
        String key = key(postId);
        Long value = redisTemplate.opsForValue().increment(key);
        return value == null ? 0L : value;
    }

    public long getRealtimeView(Long postId, long dbFallback) {
        String value = redisTemplate.opsForValue().get(key(postId));
        return (value == null) ? dbFallback : Long.parseLong(value) + dbFallback;
    }

    private String key(Long postId) {
        return "post:" + postId + ":views";
    }
}
