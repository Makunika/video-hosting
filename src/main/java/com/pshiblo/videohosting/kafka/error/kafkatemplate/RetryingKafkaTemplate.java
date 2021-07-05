package com.pshiblo.videohosting.kafka.error.kafkatemplate;

import com.vltgroup.powercasino.kafka.header.DelayHeader;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RetryingKafkaTemplate<K ,V> extends KafkaTemplate<K, V> {

  public RetryingKafkaTemplate(ProducerFactory<K, V> producerFactory) {
    super(producerFactory);
  }

  public RetryingKafkaTemplate(ProducerFactory<K, V> producerFactory, Map<String, Object> configOverrides) {
    super(producerFactory, configOverrides);
  }

  public RetryingKafkaTemplate(ProducerFactory<K, V> producerFactory, boolean autoFlush) {
    super(producerFactory, autoFlush);
  }

  public RetryingKafkaTemplate(ProducerFactory<K, V> producerFactory, boolean autoFlush, Map<String, Object> configOverrides) {
    super(producerFactory, autoFlush, configOverrides);
  }

  public ListenableFuture<SendResult<K, V>> sendDelayed(String kafkaTopic, K key, V value, Duration time) {
    return this.sendDelayed(kafkaTopic, key, value, Collections.singletonList(new DelayHeader(time)));
  }

  public ListenableFuture<SendResult<K, V>> sendDelayed(String kafkaTopic, K key, V value) {
    return this.sendDelayed(kafkaTopic, key, value, Duration.ZERO);
  }

  public ListenableFuture<SendResult<K, V>> sendDelayed(String kafkaTopic, K key, V value, Iterable<Header> headers) {
    Map<String, Object> mapHeaders = new HashMap<>();
    mapHeaders.put(KafkaHeaders.MESSAGE_KEY, key);
    mapHeaders.put(KafkaHeaders.TOPIC, kafkaTopic);
    MessageHeaders headersMessage = new MessageHeaders(mapHeaders);
    Message<?> message = new GenericMessage<>(value, headersMessage);
    ProducerRecord record = this.getMessageConverter().fromMessage(message, kafkaTopic);
    headers.forEach(header -> record.headers().add(header));
    return this.send((ProducerRecord<K, V>)record);
  }

}
