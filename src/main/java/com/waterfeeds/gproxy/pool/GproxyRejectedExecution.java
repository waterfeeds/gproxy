package com.waterfeeds.gproxy.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

public class GproxyRejectedExecution extends ThreadPoolExecutor.DiscardPolicy {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        logger.info(getClass().getName() + ": 线程池已满, {}线程遭到丢弃" + r.toString());
        super.rejectedExecution(r, e);
    }
}
