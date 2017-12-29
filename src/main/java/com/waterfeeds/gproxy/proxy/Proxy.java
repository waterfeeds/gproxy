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

    public void addServerChannel(String serverName, ServerChannel serverChannel) {
        serverChannels.put(serverName, serverChannel);
    }

    public void removeServerChannel(String serverName) {
        if (serverChannels.containsKey(serverName)) {
            serverChannels.remove(serverName);
        }
    }

    public void addServerAddress(String serverName, URI uri) {
        serverAddresses.put(serverName, uri);
        boolean status = tryConnectServer(serverName, uri);
        if (!status) {
            removeServerAddress(serverName);
        }
    }

    public void removeServerAddress(String serverName) {
        if (serverAddresses.containsKey(serverName)) {
            serverAddresses.remove(serverName);
        }
        removeServerChannel(serverName);
    }

    public void addRouterMap(String clientId, ServerChannel serverChannel) {
        routerChannelMap.put(clientId, serverChannel);
    }

    public void removeRouterMap(String clientId) {
        if (routerChannelMap.containsKey(clientId)) {
            routerChannelMap.remove(clientId);
        }
    }

    public void initServerChannels() {
        clientApiService = DefaultClientApiService.newInstance(2);
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

    public ServerChannel getRandRouteChannel(String clientId) {
        if (!routerChannelMap.containsKey(clientId)) {
            router.randRoute(clientId, serverChannels, routerChannelMap);
        }
        if (routerChannelMap.containsKey(clientId)) {
            return routerChannelMap.get(clientId);
        }
        return null;
    }

    public ServerChannel getRouteChannelByServer(String clientId, String serverName) {
        if (!routerChannelMap.containsKey(clientId)) {
            router.routeByServer(clientId, serverName, serverChannels, routerChannelMap);
        }
        if (routerChannelMap.containsKey(clientId)) {
            return routerChannelMap.get(clientId);
        }
        return null;
    }

    public ServerChannel getRouteChannel(String clientId, String serverName) {
        if (!routerChannelMap.containsKey(clientId)) {
            router.routeByServer(clientId, serverName, serverChannels, routerChannelMap);
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

    public boolean tryConnectServer(String serverName, URI uri) {
        ChannelManager manager = clientApiService.doConnect(uri);
        if (manager != null && manager.isAvailable()) {
            ServerChannel serverChannel = new ServerChannel(manager);
            addServerChannel(serverName, serverChannel);
            return true;
        } else {
            return false;
        }
    }

    public void tryConnectServers() {
        Iterator iterator = serverAddresses.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String serverName = (String) entry.getKey();
            URI uri = (URI) entry.getValue();
            if (serverChannels.containsKey(serverName)) {
                continue;
            }
            tryConnectServer(serverName, uri);
        }
    }
}
