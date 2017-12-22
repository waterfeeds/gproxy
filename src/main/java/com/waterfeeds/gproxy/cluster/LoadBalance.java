package com.waterfeeds.gproxy.cluster;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.zookeeper.RemoteAddress;

public interface LoadBalance {
    public URI selectRandom(RemoteAddress[] uris);

    public String getName();
}
