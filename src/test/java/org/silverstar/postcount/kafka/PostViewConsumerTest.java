package org.silverstar.postcount.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.silverstar.postcount.service.PostRealtimeViewCountService;
import org.silverstar.postcount.util.PostKeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;


@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {PostKeyProvider.KAFKA_TOPIC})
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
class PostViewConsumerTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockitoBean
    private PostRealtimeViewCountService postRealtimeViewCountService;

    @MockitoBean
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void setupRedisMock() {
        ValueOperations<String, String> ops = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(ops);
        when(ops.setIfAbsent(anyString(), anyString(), any(Duration.class))).thenReturn(true);
    }

    @Test
    void consumerShouldCallServiceOnce_forSingleMessage() throws ExecutionException, InterruptedException {
        String postId = "1234";
        String eventId = UUID.randomUUID().toString();

        kafkaTemplate.send(PostKeyProvider.KAFKA_TOPIC, postId, eventId).get();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(postRealtimeViewCountService, times(1)).incrementView(Long.valueOf(postId)));
    }

}
