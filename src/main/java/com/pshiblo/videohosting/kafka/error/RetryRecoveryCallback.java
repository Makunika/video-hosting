package com.pshiblo.videohosting.kafka.error;

import com.vltgroup.powercasino.kafka.header.DelayHeader;
import com.vltgroup.powercasino.kafka.kafkatemplate.RetryingKafkaTemplate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryContext;

import java.time.Duration;
import java.util.Optional;

import static org.springframework.kafka.listener.adapter.RetryingMessageListenerAdapter.CONTEXT_RECORD;

public class RetryRecoveryCallback implements RecoveryCallback<Object> {

  private final KafkaTemplate kafkaTemplate;
  private final long initMillis;
  private final long maxMinutes;

  public RetryRecoveryCallback(KafkaTemplate kafkaTemplate, long initMillis, long maxMinutes) {
    this.kafkaTemplate = kafkaTemplate;
    this.initMillis = initMillis;
    this.maxMinutes = maxMinutes;
  }

  @Override
  public Object recover(RetryContext retryContext) throws Exception {
    ConsumerRecord record = (ConsumerRecord) retryContext.getAttribute(CONTEXT_RECORD);

    Duration timeSleep = Duration.ZERO;

    if (record.headers().headers(DelayHeader.KEY).iterator().hasNext()) {
      Header header = record.headers().headers(DelayHeader.KEY).iterator().next();
      Duration timeSleepHeader = DelayHeader.getDelay(header.value());
      if (timeSleepHeader.isZero()) {
        timeSleep = Duration.ofMillis(initMillis);
      } else {
        Thread.sleep(timeSleepHeader.toMillis());
        timeSleep = Duration.ofSeconds(timeSleepHeader.getSeconds() * timeSleepHeader.getSeconds());
        if (timeSleep.toMinutes() > maxMinutes) {
          timeSleep = Duration.ofMinutes(maxMinutes);
        }
      }
    }

    kafkaTemplate.send(record);
    return Optional.empty();
  }
}
