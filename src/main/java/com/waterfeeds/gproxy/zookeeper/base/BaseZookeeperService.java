package com.waterfeeds.gproxy.zookeeper.base;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.zookeeper.RemoteAddress;
import org.apache.zookeeper.CreateMode;

public interface BaseZookeeperService {
    void registerNode(String path, URI uri, CreateMode mode, byte[] bytes, boolean is);

    void removeNode(String path, boolean is);

    boolean exists(String path);

    public RemoteAddress[] getChildNodes(String path);

    public String getData(String path);
}
