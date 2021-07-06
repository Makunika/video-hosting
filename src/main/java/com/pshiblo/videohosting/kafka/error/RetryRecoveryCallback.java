package com.pshiblo.videohosting.kafka.error;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryContext;

import java.util.Optional;

import static org.springframework.kafka.listener.adapter.RetryingMessageListenerAdapter.CONTEXT_RECORD;

public class RetryRecoveryCallback implements RecoveryCallback<Object> {

  private final KafkaTemplate kafkaTemplate;
  private final long maxMs;
  private final ObjectMapper objectMapper;

  public RetryRecoveryCallback(KafkaTemplate kafkaTemplate, long maxMs, ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.maxMs = maxMs;
    this.objectMapper = objectMapper;
  }

  @Override
  public Object recover(RetryContext retryContext) throws Exception {
    ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) retryContext.getAttribute(CONTEXT_RECORD);

    if (HeadersUtils.isAllowDelay(record.headers())) {
      long delay = HeadersUtils.readDelay(record.headers());

      Thread.sleep(delay);

      long nextDelay = delay * 2;
      if (nextDelay > maxMs) {
        nextDelay = maxMs;
      }
      JsonNode jsonNode = objectMapper.readTree(record.value());

      ProducerRecord producerRecord = new ProducerRecord(record.topic(), record.key(), jsonNode);

      HeadersUtils.setAllowDelay(producerRecord.headers());
      HeadersUtils.setDelay(producerRecord.headers(), nextDelay);
      kafkaTemplate.send(producerRecord);
    }
    return Optional.empty();
  }
}
