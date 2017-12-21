package com.waterfeeds.gproxy.proxy.handler;

import com.waterfeeds.gproxy.network.base.BaseChannelInitializer;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.ArrayList;
import java.util.List;


public class ProxyChannelInitializer extends ChannelInitializer<SocketChannel> {
    private List<ChannelInboundHandlerAdapter> handlers = new ArrayList<ChannelInboundHandlerAdapter>();

    public void init(ChannelInboundHandlerAdapter... handlers) {
        for (ChannelInboundHandlerAdapter handler: handlers) {
            this.handlers.add(handler);
        }
    }

    public void init(List<ChannelInboundHandlerAdapter> handlers) {
        this.handlers = handlers;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch = BaseChannelInitializer.baseInit(ch);
        for (ChannelInboundHandlerAdapter handler: handlers) {
            ch.pipeline().addLast(handler);
        }
    }

}
