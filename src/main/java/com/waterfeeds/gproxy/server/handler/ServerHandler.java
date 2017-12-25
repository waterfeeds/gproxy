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
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler extends ChannelInboundHandlerAdapter{
    private Server server;

    public ServerHandler(Server server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String proxyId = ChannelContextFactory.getLongId(ctx);
        ProxyChannel proxyChannel = ChannelContextFactory.getProxyChannel(ctx);
        System.out.println("connect by proxy");
        server.addProxyChannel(proxyId, proxyChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String proxyId = ChannelContextFactory.getLongId(ctx);
        server.removeProxyChannel(proxyId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String proxyId = ChannelContextFactory.getLongId(ctx);
        GproxyProtocol protocol = (GproxyProtocol) msg;
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        int cmd = header.getCmd();
        switch (cmd) {
            case GproxyCommand.SERVER_EVENT:
                String content = body.getContent();
                if ("login".equals(content)) {
                    header.setCmd(GproxyCommand.SEND_TO_ALL);
                    body.setContent("login success");
                    header.setContentLen(body.getContentLen());
                }
                try {
                    ChannelManager manager = server.getProxyChannels().get(proxyId).getManager();
                    if (manager.isAvailable()) {
                        manager.getChannel().writeAndFlush(protocol);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }
}
