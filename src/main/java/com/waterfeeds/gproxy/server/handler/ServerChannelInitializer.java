package com.waterfeeds.gproxy.server.handler;

import com.waterfeeds.gproxy.network.base.BaseChannelInitializer;
import com.waterfeeds.gproxy.protocol.tcp.TcpDecoder;
import com.waterfeeds.gproxy.protocol.tcp.TcpEncoder;
import com.waterfeeds.gproxy.server.Server;
import com.waterfeeds.gproxy.user.DefaultEventHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.logging.InternalLogLevel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private Server server;
    private List<ChannelInboundHandlerAdapter> handlers = new ArrayList<ChannelInboundHandlerAdapter>();

    public ServerChannelInitializer(Server server, ChannelInboundHandlerAdapter... handlers) {
        this.server = server;
        for (ChannelInboundHandlerAdapter handler: handlers) {
            this.handlers.add(handler);
        }
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch = BaseChannelInitializer.baseInit(ch);
        ch.pipeline().addLast(new ServerHandler(server, new DefaultEventHandler()));
    }
}
