package ru.javawebinar.topjava.service.rule;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MealTestWatcher extends TestWatcher {

    private static final Logger logger = LoggerFactory.getLogger(MealTestWatcher.class);

    private long fullTestTime = 0;

    private long startMethodTest;

    private long endMethodTest;

    @Override
    protected void starting(Description description) {
        startMethodTest = System.currentTimeMillis();
    }

    @Override
    protected void finished(Description description) {
        endMethodTest = System.currentTimeMillis();
        long testTime = endMethodTest - startMethodTest;
        fullTestTime += testTime;

        logger.info("Test - {}, test time - {} ms", description.getMethodName(), testTime);
    }

    @Override
    protected void succeeded(Description description) {
        super.succeeded(description);
    }

    public void logFullTimeTest() {
        logger.info("Test full time - {} ms",  fullTestTime);
    }
}
