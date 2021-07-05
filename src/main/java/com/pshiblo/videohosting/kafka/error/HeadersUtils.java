package com.pshiblo.videohosting.kafka.error;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

public class HeadersUtils {
    private static final String ALLOW_DELAY = "allow_delay";
    private static final String ORIGIN_TOPIC = "origin_topic";
    private static final String DELAY_UNTIL = "delay_until";
    private static final String ATTEMPTS = "attempts";

    public static void setAllowDelay(Headers headers) {
        headers.add(ALLOW_DELAY, "true".getBytes());
    }

    public static boolean isAllowDelay(Headers headers) {
        String allow = readHeader(headers, ALLOW_DELAY);
        return allow != null;
    }

    public static void setOriginTopic(Headers headers, String originTopic) {
        headers.add(ALLOW_DELAY, originTopic.getBytes());
    }

    public static String readOriginTopic(Headers headers) {
        return readHeader(headers, ORIGIN_TOPIC);
    }

    public static void setDelayUntil(Headers headers, long delayMs) {
        headers.add(ALLOW_DELAY, Long.toString(System.currentTimeMillis() + delayMs).getBytes());
    }

    public static long readDelayUntil(Headers headers) {
        return Long.parseLong(readHeader(headers, DELAY_UNTIL));
    }

    public static void setAttempts(Headers headers, int attempts) {
        headers.add(ALLOW_DELAY, Integer.toString(attempts).getBytes());
    }

    public static int readAttempts(Headers headers) {
        return Integer.parseInt(readHeader(headers, ATTEMPTS));
    }

    private static String readHeader(Headers headers, String headerString) {
        Header header = headers.lastHeader(headerString);
        if (header == null) {
            return null;
        }
        return new String(header.value());
    }
}
