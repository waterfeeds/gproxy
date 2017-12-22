package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.server.channel.ProxyChannel;

import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private String id;
    private String name;
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

    public ConcurrentHashMap<String, ProxyChannel> getProxyChannels() {
        return proxyChannels;
    }

    public void setProxyChannels(ConcurrentHashMap<String, ProxyChannel> proxyChannels) {
        this.proxyChannels = proxyChannels;
    }

    public void addProxyChannel(String address, ProxyChannel proxyChannel) {
        proxyChannels.put(address, proxyChannel);
    }

    public void removeProxyChannel(String address) {
        proxyChannels.remove(address);
    }

    public void initProxyChannels() {
        proxyChannels = new ConcurrentHashMap<String, ProxyChannel>();
    }
}
