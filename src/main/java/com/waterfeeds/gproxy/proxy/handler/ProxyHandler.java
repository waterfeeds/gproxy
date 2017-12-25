package com.waterfeeds.gproxy.proxy.handler;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.ChannelContextFactory;
import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.proxy.Proxy;
import com.waterfeeds.gproxy.proxy.channel.ClientChannel;
import com.waterfeeds.gproxy.proxy.channel.ServerChannel;
import com.waterfeeds.gproxy.server.channel.ProxyChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;

public class ProxyHandler extends ChannelInboundHandlerAdapter {
    private Proxy proxy;

    public ProxyHandler(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String clientId = ChannelContextFactory.getLongId(ctx);
        System.out.println(clientId);
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
        int cmd = header.getCmd();
        switch (cmd) {
            case GproxyCommand.SERVER_EVENT:
                ServerChannel serverChannel = proxy.getRouteChannel(clientId);
                if (serverChannel.getManager().isAvailable()) {
                    serverChannel.getManager().getChannel().writeAndFlush(msg);
                }
                break;
            default:
                break;
        }
    }
}
