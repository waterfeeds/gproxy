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
    private SocketChannel socketChannel;

    public BaseChannelInitializer() {
        socketChannel.pipeline().addLast(new LoggingHandler(String.valueOf(InternalLogLevel.INFO)));
        socketChannel.pipeline().addLast(new TcpEncoder());
        socketChannel.pipeline().addLast(new TcpDecoder());
        socketChannel.pipeline().addLast(new IdleStateHandler(30, 30, 30));
        socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                super.channelActive(ctx);
                System.out.println(ctx.channel().remoteAddress() + " connected");
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                GproxyProtocol protocol = (GproxyProtocol) msg;
                System.out.println("received content: " + protocol.getBody().getContent());
            }
        });
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
}
