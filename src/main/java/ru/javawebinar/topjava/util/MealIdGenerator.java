package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicLong;

public class MealIdGenerator {
    private static final AtomicLong idCount = new AtomicLong();

    public static long generateId() {
        return idCount.incrementAndGet();
    }
}
