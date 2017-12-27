package com.waterfeeds.gproxy.server.base;

import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.server.Server;

public abstract class Callback {
    private String proxyId;
    protected Server server;
    public abstract void onMessage(GproxyProtocol protocol);

    public void setProxyId(String proxyId) {
        this.proxyId = proxyId;
    }

    public String getProxyId() {
        return this.proxyId;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
