package com.waterfeeds.gproxy.zookeeper.base;

import com.waterfeeds.gproxy.message.URI;

public interface NodeEventHandler {
    void addNode(String serviceName, URI uri);

    void removeNode(String serviceName);

    void updateNode(String serviceName, URI uri);
}
