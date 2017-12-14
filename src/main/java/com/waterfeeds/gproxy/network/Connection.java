package com.waterfeeds.gproxy.network;

import io.netty.channel.ChannelFuture;

public interface Connection {
    public ChannelFuture init();
}
