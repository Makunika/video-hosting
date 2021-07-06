package com.pshiblo.videohosting.kafka.error;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.messaging.MessageHeaders;

public class HeadersUtils {
    private static final String ALLOW_DELAY = "allow_delay";
    private static final String DELAY = "delay";

    public static void setAllowDelay(Headers headers) {
        headers.remove(ALLOW_DELAY);
        headers.add(ALLOW_DELAY, "true".getBytes());
    }

    public static boolean isAllowDelay(Headers headers) {
        String allow = readHeader(headers, ALLOW_DELAY);
        return allow != null;
    }

    public static void setDelay(Headers headers, long delayMs) {
        headers.remove(DELAY);
        headers.add(DELAY, Long.toString(delayMs).getBytes());
    }

    public static long readDelay(Headers headers) {
        return Long.parseLong(readHeader(headers, DELAY));
    }

    public static long readDelay(MessageHeaders headers) {
        return Long.parseLong(readHeader(headers, DELAY));
    }

    public static boolean containDelay(Headers headers) {
        return readHeader(headers, DELAY) != null;
    }

    private static String readHeader(Headers headers, String headerString) {
        Header header = headers.lastHeader(headerString);
        if (header == null) {
            return null;
        }
        return new String(header.value());
    }

    private static String readHeader(MessageHeaders headers, String headerString) {
        Object header = headers.getOrDefault(headerString, null);
        if (!(header instanceof String)) {
            return null;
        }
        return (String) header;
    }
}
