package com.pshiblo.videohosting.kafka.error;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.time.Duration;
import java.util.Map;

public class RetryKafkaProducerInterceptor implements ProducerInterceptor<String, Object> {

    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> producerRecord) {
        if (producerRecord.value() instanceof KafkaAllowDelay && ((KafkaAllowDelay) producerRecord.value()).isAllowDelay()) {
            HeadersUtils.setAllowDelay(producerRecord.headers());
            if (!HeadersUtils.containDelay(producerRecord.headers())) {
                HeadersUtils.setDelay(producerRecord.headers(), 500L);
            }
        }
        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}