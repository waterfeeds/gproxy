package com.waterfeeds.gproxy.proxy.handler;

import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.proxy.Proxy;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ForwardHandler extends ChannelInboundHandlerAdapter {
    private Proxy proxy;

    public ForwardHandler(Proxy proxy) {
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
        int cmd = protocol.getHeader().getCmd();
        switch (cmd) {
            case GproxyCommand.SEND_TO_ALL:
                proxy.sendToAll(protocol);
                break;
            case GproxyCommand.SEND_TO_CLIENT:
                break;
            case GproxyCommand.SEND_TO_USER:
                break;
            case GproxyCommand.SEND_TO_GROUP:
                break;
            case GproxyCommand.GET_CLIENT_COUNT:
                break;
            case GproxyCommand.GET_USER_COUNT:
                break;
            case GproxyCommand.GET_GROUP_COUNT:
                break;
        }
    }
}
