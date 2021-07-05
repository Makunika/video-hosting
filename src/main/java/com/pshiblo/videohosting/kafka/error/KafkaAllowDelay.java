package com.pshiblo.videohosting.kafka.error;

public interface KafkaAllowDelay {

    default boolean isAllowDelay() {
        return true;
    }
}
