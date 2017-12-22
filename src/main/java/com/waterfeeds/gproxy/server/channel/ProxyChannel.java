package com.waterfeeds.gproxy.server.channel;

import com.waterfeeds.gproxy.network.ChannelManager;

public class ProxyChannel {
    private ChannelManager manager;

    public ProxyChannel(ChannelManager manager) {
        this.manager = manager;
    }

    public ChannelManager getManager() {
        return manager;
    }

    public void setManager(ChannelManager manager) {
        this.manager = manager;
    }
}
