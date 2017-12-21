package com.waterfeeds.gproxy.proxy.channel;

import com.waterfeeds.gproxy.network.ChannelManager;

public class ServerChannel {
    private ChannelManager manager;

    public ChannelManager getManager() {
        return manager;
    }

    public void setManager(ChannelManager manager) {
        this.manager = manager;
    }
}
