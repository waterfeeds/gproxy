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

import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private Server server;
    private AbstractEventHandler eventHandler;

    public ServerHandler(Server server, AbstractEventHandler eventHandler) {
        this.server = server;
        this.eventHandler = eventHandler;
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
        Channel channel = ctx.channel();
        String address = channel.remoteAddress().toString().substring(1);
        GproxyProtocol protocol = eventHandler.handleEvent((GproxyProtocol) msg);
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        int cmd = header.getCmd();
        switch (cmd) {
            case GproxyCommand.SEND_TO_CLIENT:
                ChannelManager manager = server.getProxyChannels().get(proxyId).getManager();
                if (manager.isAvailable()) {
                    manager.getChannel().writeAndFlush(protocol);
                }
        }
    }
}
