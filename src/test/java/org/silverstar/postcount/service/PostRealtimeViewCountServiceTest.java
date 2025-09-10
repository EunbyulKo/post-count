package org.silverstar.postcount.service;

import org.junit.jupiter.api.Test;
import org.silverstar.postcount.support.ConcurrentTestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest
class PostRealtimeViewCountServiceTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379)
            .withCommand("redis-server", "--appendonly", "yes");

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry r) {
        r.add("spring.data.redis.host", () -> redis.getHost());
        r.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void incr() {
        Long v = redisTemplate.opsForValue().increment("post:1:views");
        assertThat(v).isNotNull();
    }

    @Test
    void incr_is_atomic_under_contention() throws Exception {
        // given
        String key = "post:42:views";
        redisTemplate.delete(key);

        int threads = 32;
        int perThreadOps = 10000;

        // when
        List<Future<?>> futures = ConcurrentTestTemplate.run(
                threads,
                threads,
                () -> {
                    for (int i = 0; i < perThreadOps; i++) {
                        redisTemplate.opsForValue().increment(key);
                    }
                }
        );

        for (Future<?> f : futures) {
            f.get(10, TimeUnit.SECONDS);
        }

        // then
        long total = Long.parseLong(redisTemplate.opsForValue().get(key));
        assertThat(total).isEqualTo((long) threads * perThreadOps);
    }

}