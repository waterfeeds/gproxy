package com.waterfeeds.gproxy.proxy;

import com.alibaba.dubbo.remoting.Server;
import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.network.DefaultClientApiService;
import com.waterfeeds.gproxy.proxy.base.AbstractProxy;
import com.waterfeeds.gproxy.proxy.channel.ServerChannel;
import com.waterfeeds.gproxy.proxy.router.Router;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Proxy extends AbstractProxy {
    private int proxyId;
    private DefaultClientApiService clientApiService;
    private ConcurrentHashMap<String, ServerChannel> serverChannels;
    private ConcurrentHashMap<String, URI> serverAddresses;
    private HashMap<String, ServerChannel> routerChannelMap;
    private Router router;

    public Proxy() {
        initServerChannels();
    }

    public Proxy(int proxyId) {
        this.proxyId = proxyId;
        initServerChannels();
    }

    public DefaultClientApiService getClientApiService() {
        return clientApiService;
    }

    public void setClientApiService(DefaultClientApiService clientApiService) {
        this.clientApiService = clientApiService;
    }

    public ConcurrentHashMap<String, ServerChannel> getServerChannels() {
        return serverChannels;
    }

    public void setServerChannels(ConcurrentHashMap<String, ServerChannel> serverChannels) {
        this.serverChannels = serverChannels;
    }

    public ConcurrentHashMap<String, URI> getServerAddresses() {
        return serverAddresses;
    }

    public void setServerAddresses(ConcurrentHashMap<String, URI> serverAddresses) {
        this.serverAddresses = serverAddresses;
        tryConnectServers();
    }

    public void addServerChannel(String serverId, ServerChannel serverChannel) {
        serverChannels.put(serverId, serverChannel);
    }

    public void removeServerChannel(String serverId) {
        serverChannels.remove(serverId);
    }

    public void addServerAddress(String serverId, URI uri) {
        serverAddresses.put(serverId, uri);
        tryConnectServer(serverId, uri);
    }

    public void removeServerAddress(String serverId, URI uri) {
        serverAddresses.remove(serverId);
        removeServerChannel(serverId);
    }

    public void initServerChannels() {
        clientApiService = DefaultClientApiService.newInstance(1);
        serverAddresses = new ConcurrentHashMap<String, URI>();
        serverChannels = new ConcurrentHashMap<String, ServerChannel>();
        routerChannelMap = new HashMap<String, ServerChannel>();
        router = new Router();
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

    public ServerChannel getClientServerChannel(String clientId, String serverId) {
        if (!routerChannelMap.containsKey(clientId)) {
            router.routeByServer(clientId, serverId, serverChannels, routerChannelMap);
        }
        if (routerChannelMap.containsKey(clientId)) {
            return routerChannelMap.get(clientId);
        }
        return null;
    }

    public void tryConnectServer(String serverId, URI uri) {
        ChannelManager manager = clientApiService.doConnect(uri);
        if (manager.isAvailable()) {
            ServerChannel serverChannel = new ServerChannel(manager);
            addServerChannel(serverId, serverChannel);
        }
    }

    public void tryConnectServers() {
        Iterator iterator = serverAddresses.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String serverId = (String) entry.getKey();
            URI uri = (URI) entry.getValue();
            if (serverChannels.containsKey(serverId)) {
                continue;
            }
            ChannelManager manager = clientApiService.doConnect(uri);
            if (manager.isAvailable()) {
                ServerChannel serverChannel = new ServerChannel(manager);
                addServerChannel(serverId, serverChannel);
            }
        }
    }
}
