package com.waterfeeds.gproxy.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class GproxyExecutor extends AbstractGproxyExecutor {
    public GproxyExecutor(String name) {
        super(name);
    }

    public GproxyExecutor(String name, int corePoolSize, int maxPoolSize, int blockQueueSize) {
        super(name, corePoolSize, maxPoolSize, blockQueueSize);
    }

    public void execute(Runnable runnable) {
        service.execute(runnable);
    }

    public <T> Future<T> submit(Callable<T> runnable) {
        return (Future<T>) service.submit(runnable);
    }
}
