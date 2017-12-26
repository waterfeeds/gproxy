package com.waterfeeds.gproxy.proxy.handler;

import com.waterfeeds.gproxy.network.ChannelContextFactory;
import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.proxy.Proxy;
import com.waterfeeds.gproxy.proxy.channel.ClientChannel;
import com.waterfeeds.gproxy.proxy.channel.ServerChannel;
import com.waterfeeds.gproxy.protocol.base.BaseEventConverter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProxyHandler extends ChannelInboundHandlerAdapter {
    private Proxy proxy;

    public ProxyHandler(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String clientId = ChannelContextFactory.getLongId(ctx);
        ClientChannel clientChannel = ChannelContextFactory.getClientChannel(ctx);
        proxy.addClientChannel(clientId, clientChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String clientId = ChannelContextFactory.getLongId(ctx);
        proxy.removeClientChannel(clientId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String clientId = ChannelContextFactory.getLongId(ctx);
        GproxyProtocol protocol = (GproxyProtocol) msg;
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        String content = body.getContent();
        int cmd = header.getCmd();
        switch (cmd) {
            case GproxyCommand.CLIENT_EVENT:
                protocol = BaseEventConverter.converterByClientId(protocol, clientId, content);
                ServerChannel serverChannel = proxy.getRouteChannel(clientId);
                if (serverChannel.getManager().isAvailable()) {
                    serverChannel.getManager().getChannel().writeAndFlush(protocol);
                }
                break;
            default:
                break;
        }
    }
}
