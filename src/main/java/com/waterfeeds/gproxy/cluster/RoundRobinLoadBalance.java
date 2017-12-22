package com.waterfeeds.gproxy.cluster;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.zookeeper.RemoteAddress;

import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalance implements LoadBalance {
    String name = "robin";

    private static final AtomicInteger index = new AtomicInteger(0);

    @Override
    public URI selectRandom(RemoteAddress[] uris) {
        if (uris != null && uris.length > 0) {
            int length = uris.length;
            int nextIndex = index.incrementAndGet();
            int robinIndex = nextIndex % length;
            return uris[robinIndex].getUri();
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
