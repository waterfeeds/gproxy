package com.waterfeeds.gproxy.server.handler;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.ChannelContextFactory;
import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.server.Server;
import com.waterfeeds.gproxy.server.channel.ProxyChannel;
import com.waterfeeds.gproxy.user.DefaultEventHandler;
import com.waterfeeds.gproxy.user.base.AbstractEventHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ServerChannel;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private Server server;

    public ServerHandler(Server server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String proxyId = ChannelContextFactory.getLongId(ctx);
        ProxyChannel proxyChannel = ChannelContextFactory.getProxyChannel(ctx);
        server.addProxyChannel(proxyId, proxyChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String proxyId = ChannelContextFactory.getLongId(ctx);
        server.removeProxyChannel(proxyId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String proxyId = ChannelContextFactory.getLongId(ctx);
        GproxyProtocol protocol = (GproxyProtocol) msg;
        int cmd = protocol.getHeader().getCmd();
        switch (cmd) {
            case GproxyCommand.SEND_TO_ALL:
                sendToAllProxy(protocol);
                break;
            case GproxyCommand.SEND_TO_USER:
                sendToAllProxy(protocol);
                break;
            case GproxyCommand.SEND_TO_GROUP:
                sendToAllProxy(protocol);
                break;
            case GproxyCommand.SEND_TO_CLIENT:
                ChannelManager manager = server.getProxyChannels().get(proxyId).getManager();
                if (manager.isAvailable()) {
                    manager.getChannel().writeAndFlush(protocol);
                }
                break;
        }
    }

    public void sendToAllProxy(GproxyProtocol protocol) {
        Iterator iterator = server.getProxyChannels().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            ProxyChannel proxyChannel = (ProxyChannel) entry.getValue();
            ChannelManager manager = proxyChannel.getManager();
            if (manager.isAvailable()) {
                manager.getChannel().writeAndFlush(protocol);
            }
        }
    }

}
