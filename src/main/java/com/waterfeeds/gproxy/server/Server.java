package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.server.base.AbstractServer;
import com.waterfeeds.gproxy.server.channel.ProxyChannel;

import java.util.concurrent.ConcurrentHashMap;

public class Server extends AbstractServer {
    private String id;
    private String name;
    private URI uri;
    private ConcurrentHashMap<String, ProxyChannel> proxyChannels;

    public Server() {
        initProxyChannels();
    }

    public Server(String serverId) {
        this.id = serverId;
        initProxyChannels();
    }

    public Server(String serverId, String serverName) {
        this.id = serverId;
        this.name = serverName;
        initProxyChannels();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public ConcurrentHashMap<String, ProxyChannel> getProxyChannels() {
        return proxyChannels;
    }

    public void setProxyChannels(ConcurrentHashMap<String, ProxyChannel> proxyChannels) {
        this.proxyChannels = proxyChannels;
    }

    public void addProxyChannel(String proxyId, ProxyChannel proxyChannel) {
        proxyChannels.put(proxyId, proxyChannel);
    }

    public void removeProxyChannel(String proxyId) {
        proxyChannels.remove(proxyId);
    }

    public void initProxyChannels() {
        proxyChannels = new ConcurrentHashMap<String, ProxyChannel>();
    }
}
