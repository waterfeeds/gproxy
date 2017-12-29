package com.waterfeeds.gproxy.network.base;

import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.tcp.TcpDecoder;
import com.waterfeeds.gproxy.protocol.tcp.TcpEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.logging.InternalLogLevel;

public class BaseChannelInitializer {

    public static SocketChannel baseInit(SocketChannel ch) {
        ch.pipeline().addLast(new LoggingHandler(String.valueOf(InternalLogLevel.INFO)));
        ch.pipeline().addLast(new TcpEncoder());
        ch.pipeline().addLast(new TcpDecoder());
        ch.pipeline().addLast(new IdleStateHandler(30, 30, 30));
        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                super.channelActive(ctx);
                System.out.println(ctx.channel().remoteAddress() + " connected");
            }

            @Override
            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                super.channelInactive(ctx);
                System.out.println(ctx.channel().remoteAddress() + " disconnected");
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                GproxyProtocol protocol = (GproxyProtocol) msg;
                System.out.println(ctx.channel().remoteAddress() + " " + protocol.getBody().getContent());
                ctx.fireChannelRead(msg);
            }
        });
        return ch;
    }

}
