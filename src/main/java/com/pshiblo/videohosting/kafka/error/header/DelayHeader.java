package com.pshiblo.videohosting.kafka.error.header;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.header.Header;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class DelayHeader implements Header {

  public static final String KEY = "delay_retry";

  private final String delay;

  public DelayHeader(Duration delay) {
    this(delay.toMillis());
  }

  public DelayHeader(long millis) {
    this(String.valueOf(millis));
  }

  public DelayHeader(String millis) {
    this.delay = String.valueOf(millis);
  }

  @Override
  public String key() {
    return KEY;
  }

  @Override
  public byte[] value() {
    return delay.getBytes(StandardCharsets.UTF_8);
  }

  public static Duration getDelay(byte[] valueHeader) {
    return Duration.ofMillis(
      Long.parseLong(
        StringUtils.toEncodedString(valueHeader, StandardCharsets.UTF_8)
      )
    );
  }
}
