package com.url.shortener.helper;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class Helper {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String longToBase64(long value) {
        byte[] bytes = ByteBuffer.allocate(Long.BYTES).putLong(value).array();
        // URL-safe, no padding
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static long base64ToLong(String base64) {
        byte[] bytes = Base64.getUrlDecoder().decode(base64);
        return ByteBuffer.wrap(bytes).getLong();
    }


    public static String generateShortUrl(Long uniqueId) {
        return Helper.longToBase64(uniqueId);
    }
}
