package org.silverstar.postcount.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.silverstar.postcount.service.PostRealtimeViewCountService;
import org.silverstar.postcount.util.PostKeyProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class PostViewConsumer {

  private final StringRedisTemplate redis;
  private final PostRealtimeViewCountService postRealtimeViewCountService;

  @KafkaListener(topics = PostKeyProvider.KAFKA_TOPIC, groupId = "view-counter")
  public void onMessage(ConsumerRecord<String, String> rec) {

    // 중복 요청 방지
    String eventId = rec.value();
    String dupKey = PostKeyProvider.REDIS_DUP_KEY + eventId;
    Boolean first = redis.opsForValue().setIfAbsent(dupKey, "1", Duration.ofHours(6));
    if (Boolean.FALSE.equals(first)) {
      return;
    }

    postRealtimeViewCountService.incrementView(Long.valueOf(rec.key()));
  }
}
