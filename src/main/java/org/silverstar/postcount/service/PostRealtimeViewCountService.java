package org.silverstar.postcount.service;

import lombok.RequiredArgsConstructor;
import org.silverstar.postcount.util.PostKeyProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostRealtimeViewCountService {

    private final StringRedisTemplate redisTemplate;

    public long incrementView(Long postId) {
        String key = PostKeyProvider.redisKey(postId);
        Long value = redisTemplate.opsForValue().increment(key);
        return value == null ? 0L : value;
    }

    public long getRealtimeView(Long postId, long dbFallback) {
        String value = redisTemplate.opsForValue().get(PostKeyProvider.redisKey(postId));
        return (value == null) ? dbFallback : Long.parseLong(value) + dbFallback;
    }

}
