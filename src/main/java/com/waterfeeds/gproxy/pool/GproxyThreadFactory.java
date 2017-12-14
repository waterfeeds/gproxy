package com.waterfeeds.gproxy.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class GproxyThreadFactory implements ThreadFactory{
    String name;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    public GproxyThreadFactory(String name) {
        this.name = name;
    }

    AtomicInteger number = new AtomicInteger(0);
    public Thread newThread(Runnable runnable) {
        logger.info(this + ":create name:{}--{}线程成功", name, number.incrementAndGet());
        String newName = name + "-" + number.intValue();
        Thread result = new Thread(runnable,newName);
        return result;
    }
}
