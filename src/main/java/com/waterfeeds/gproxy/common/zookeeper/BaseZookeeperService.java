package com.waterfeeds.gproxy.common.zookeeper;

import com.waterfeeds.gproxy.common.message.URI;
import org.apache.zookeeper.CreateMode;

public interface BaseZookeeperService {
    void registerNode(String path, URI uri, CreateMode mode, boolean is);
    void removeNode(String path, boolean is);
    boolean exists(String path);
    public RemoteAddress[] getChildNodes(String path);
    public URI getData(String path);
}
