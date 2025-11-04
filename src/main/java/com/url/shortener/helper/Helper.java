package com.url.shortener.helper;

import java.util.UUID;

public class Helper {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
