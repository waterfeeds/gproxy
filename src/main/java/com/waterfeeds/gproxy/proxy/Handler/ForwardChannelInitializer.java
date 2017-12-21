package com.waterfeeds.gproxy.proxy.Handler;

import com.waterfeeds.gproxy.network.base.BaseChannelInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ForwardChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch = new BaseChannelInitializer().getSocketChannel();
    }
}
