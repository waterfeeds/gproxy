package com.waterfeeds.gproxy.proxy.handler;

import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.proxy.Proxy;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProxyHandler extends ChannelInboundHandlerAdapter {
    private Proxy proxy;

    public ProxyHandler(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GproxyProtocol protocol = (GproxyProtocol) msg;
    }
}
