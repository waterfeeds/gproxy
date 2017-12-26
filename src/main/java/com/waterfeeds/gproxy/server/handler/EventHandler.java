package com.waterfeeds.gproxy.server.handler;

import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.server.Server;
import com.waterfeeds.gproxy.user.base.AbstractEventHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EventHandler extends ChannelInboundHandlerAdapter{
    private AbstractEventHandler eventHandler;

    public EventHandler(AbstractEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GproxyProtocol protocol = eventHandler.handleEvent((GproxyProtocol) msg);
        ctx.fireChannelRead(protocol);
    }
}
