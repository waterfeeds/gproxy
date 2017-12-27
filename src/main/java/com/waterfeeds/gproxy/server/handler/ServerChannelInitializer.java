package com.waterfeeds.gproxy.server.handler;

import com.waterfeeds.gproxy.network.base.BaseChannelInitializer;
import com.waterfeeds.gproxy.server.BaseServer;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.ArrayList;
import java.util.List;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private BaseServer baseServer;
    private List<ChannelInboundHandlerAdapter> handlers = new ArrayList<ChannelInboundHandlerAdapter>();

    public ServerChannelInitializer(BaseServer baseServer, ChannelInboundHandlerAdapter... handlers) {
        this.baseServer = baseServer;
        for (ChannelInboundHandlerAdapter handler: handlers) {
            this.handlers.add(handler);
        }
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch = BaseChannelInitializer.baseInit(ch);
        ch.pipeline().addLast(new ServerHandler(baseServer));
    }
}
