package com.waterfeeds.gproxy.proxy.handler;

import com.waterfeeds.gproxy.network.base.BaseChannelInitializer;
import com.waterfeeds.gproxy.proxy.Proxy;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.ArrayList;
import java.util.List;

public class ForwardChannelInitializer extends ChannelInitializer<SocketChannel> {
    private Proxy proxy;
    private List<ChannelInboundHandlerAdapter> handlers = new ArrayList<ChannelInboundHandlerAdapter>();

    public ForwardChannelInitializer(Proxy proxy, ChannelInboundHandlerAdapter... handlers) {
        this.proxy = proxy;
        for (ChannelInboundHandlerAdapter handler: handlers) {
            this.handlers.add(handler);
        }
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch = BaseChannelInitializer.baseInit(ch);
        ch.pipeline().addLast(new ForwardHandler(proxy));
    }
}
