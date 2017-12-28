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
    private URI uri;
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

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
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

    public void addServerAddress(String serverName, URI uri) {
        System.out.println(serverName);
        serverAddresses.put(serverName, uri);
        boolean status = tryConnectServer(uri);
        if (!status) {
            serverAddresses.remove(serverName);
        }
    }

    public void removeServerAddress(String serverName, URI uri) {
        serverAddresses.remove(serverName);
        removeServerChannel(serverName);
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

    public ServerChannel getRouteChannel(String clientId) {
        if (!routerChannelMap.containsKey(clientId)) {
            router.randRoute(clientId, serverChannels, routerChannelMap);
        }
        if (routerChannelMap.containsKey(clientId)) {
            return routerChannelMap.get(clientId);
        }
        return null;
    }

    public ServerChannel getRouteChannel(String clientId, String serverId) {
        if (!routerChannelMap.containsKey(clientId)) {
            router.routeByServer(clientId, serverId, serverChannels, routerChannelMap);
        }
        if (routerChannelMap.containsKey(clientId)) {
            return routerChannelMap.get(clientId);
        }
        return null;
    }

    public void removeRouteChannel(String clientId) {
        if (routerChannelMap.containsKey(clientId)) {
            routerChannelMap.remove(clientId);
        }
    }

    public boolean tryConnectServer(URI uri) {
        ChannelManager manager = clientApiService.doConnect(uri);
        if (manager != null && manager.isAvailable()) {
            return true;
        } else {
            return false;
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
            tryConnectServer(uri);
        }
    }
}
