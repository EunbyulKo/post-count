package org.silverstar.postcount.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.silverstar.postcount.util.PostKeyProvider;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostViewProducer {

  private final KafkaTemplate<String, String> kafka;

  public void publishView(String postId, String eventId) {
    var record = new ProducerRecord<>(PostKeyProvider.KAFKA_TOPIC, postId, eventId);
    kafka.send(record);
  }
}
