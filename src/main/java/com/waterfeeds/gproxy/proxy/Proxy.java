package com.waterfeeds.gproxy.proxy;

import com.waterfeeds.gproxy.proxy.base.AbstractProxy;
import com.waterfeeds.gproxy.proxy.channel.ServerChannel;
import com.waterfeeds.gproxy.proxy.router.Router;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Proxy extends AbstractProxy {
    private int proxyId;
    private CopyOnWriteArrayList<ServerChannel> serverChannels;
    private HashMap<String, ServerChannel> routerChannelMap;
    private Router router;

    public Proxy() {
        initServerChannels();
    }

    public Proxy(int proxyId) {
        this.proxyId = proxyId;
        initServerChannels();
    }

    public CopyOnWriteArrayList<ServerChannel> getServerChannels() {
        return serverChannels;
    }

    public void setServerChannels(CopyOnWriteArrayList<ServerChannel> serverChannels) {
        this.serverChannels = serverChannels;
    }

    public void addServerChannel(ServerChannel serverChannel) {
        serverChannels.add(serverChannel);
    }

    public void removeServerChannel(ServerChannel serverChannel) {
        serverChannels.remove(serverChannel);
    }

    public void initServerChannels() {
        serverChannels = new CopyOnWriteArrayList<ServerChannel>();
        routerChannelMap = new HashMap<String, ServerChannel>();
    }

    public void resetServerChannels() {
        serverChannels.clear();
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }
}
