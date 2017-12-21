package com.waterfeeds.gproxy.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface AbstractExecutor {
    void execute(Runnable runnable);

    <T> Future<T> submit(Callable<T> runnable);
}
