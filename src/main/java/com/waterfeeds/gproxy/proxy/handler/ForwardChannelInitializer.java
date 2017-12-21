package com.waterfeeds.gproxy.proxy.handler;

import com.waterfeeds.gproxy.network.base.BaseChannelInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ForwardChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch = BaseChannelInitializer.baseInit(ch);
    }
}
