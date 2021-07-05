package com.pshiblo.videohosting.kafka.error.kafkatemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vltgroup.powercasino.kafka.header.DelayHeader;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.Duration;
import java.util.Collections;

public class DefaultKafkaTemplate {

  private final RetryingKafkaTemplate<String, String> retryingKafkaTemplate;
  private final ObjectMapper objectMapper;

  public DefaultKafkaTemplate(RetryingKafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
    this.retryingKafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public <T> ListenableFuture<SendResult<String, String>> send(String kafkaTopic, String key, T value, Duration delay) {
    try {
      String valueJson = objectMapper.writeValueAsString(value);
      return retryingKafkaTemplate.sendDelayed(kafkaTopic, key, valueJson, Collections.singletonList(new DelayHeader(delay)));
    } catch (JsonProcessingException e) {
      throw new KafkaException(e);
    }
  }

  public <T> ListenableFuture<SendResult<String, String>> send(String kafkaTopic, String key, T value) {
    return this.send(kafkaTopic, key, value, Duration.ZERO);
  }

  public RetryingKafkaTemplate<String, String> getRetryingKafkaTemplate() {
    return retryingKafkaTemplate;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }
}
