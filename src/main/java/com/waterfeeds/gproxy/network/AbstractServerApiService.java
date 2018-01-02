package com.waterfeeds.gproxy.network;

import com.alibaba.dubbo.rpc.Invocation;
import com.waterfeeds.gproxy.cache.Cache;
import com.waterfeeds.gproxy.cache.CacheFactory;
import com.waterfeeds.gproxy.pool.GproxyExecutor;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServerApiService {
    public static Cache<String, Integer> properties = new CacheFactory<String, Integer>();
    protected static final Logger log = LoggerFactory.getLogger(AbstractServerApiService.class.getSimpleName());
    protected static GproxyExecutor executor = new GproxyExecutor("gproxy-server");

    protected void invoke(Channel channel, Invocation invocation) {
        try {
            executor.execute(getSubmitTask(channel, invocation));
        } catch (Exception e) {
            log.error(this.getClass().getName() + "线程池执行失败 类{}", invocation.getClass());
        }
    }

    public abstract Runnable getSubmitTask(Channel channel, Invocation invocation);
}
