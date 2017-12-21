package com.waterfeeds.gproxy.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractGproxyExecutor implements AbstractExecutor {
    ExecutorService service;

    private int corePoolSize = 100;
    private int maxPoolSize = 200;
    private int blockQueueSize = 100;

    public ExecutorService getService() {
        return service;
    }

    public void setService(ExecutorService service) {
        this.service = service;
    }

    public AbstractGproxyExecutor(String name) {
        service = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(this.blockQueueSize), new GproxyThreadFactory(name), new GproxyRejectedExecution());
    }

    public AbstractGproxyExecutor(String name, int corePoolSize, int maxPoolSize, int blockQueueSize) {
        service = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(blockQueueSize), new GproxyThreadFactory(name), new GproxyRejectedExecution());
    }

}
