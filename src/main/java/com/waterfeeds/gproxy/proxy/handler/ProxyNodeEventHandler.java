package com.waterfeeds.gproxy.proxy.handler;

import com.waterfeeds.gproxy.message.Const;
import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.proxy.Proxy;
import com.waterfeeds.gproxy.zookeeper.base.NodeEventHandler;

public class ProxyNodeEventHandler implements NodeEventHandler {
    private Proxy proxy;

    public ProxyNodeEventHandler(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void addNode(String serverName, URI uri) {
        serverName = parseServerName(serverName);
        proxy.addServerAddress(serverName, uri);
    }

    @Override
    public void removeNode(String serverName) {
        serverName = parseServerName(serverName);
        proxy.removeServerAddress(serverName);
    }

    @Override
    public void updateNode(String serverName, URI uri) {
        serverName = parseServerName(serverName);
        proxy.removeServerAddress(serverName);
        proxy.addServerAddress(serverName, uri);
    }

    public String parseServerName(String serverName) {
        return serverName.substring(Const.ZOOKEEPER_NAMESPACE_SERVERS.length() + 2);
    }
}
