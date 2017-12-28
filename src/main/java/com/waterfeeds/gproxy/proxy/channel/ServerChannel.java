package com.waterfeeds.gproxy.proxy.channel;

import com.waterfeeds.gproxy.network.ChannelManager;
import io.netty.channel.Channel;

public class ServerChannel {
    private ChannelManager manager;

    public ServerChannel(ChannelManager manager) {
        this.manager = manager;
    }

    public ChannelManager getManager() {
        return manager;
    }

    public void setManager(ChannelManager manager) {
        this.manager = manager;
    }

    public boolean isAvailable() {
        return manager.isAvailable();
    }

    public Channel getChannel() {
        return manager.getChannel();
    }
}
