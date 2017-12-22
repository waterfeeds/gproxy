package com.waterfeeds.gproxy.server.handler;

import com.waterfeeds.gproxy.message.URI;
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

public class ServerHandler extends ChannelInboundHandlerAdapter{
    private Server server;

    public ServerHandler(Server server) {
        this.server = server;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String address = channel.remoteAddress().toString().substring(1);
        server.removeProxyChannel(address);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String address = channel.remoteAddress().toString().substring(1);
        ChannelManager manager = new ChannelManager(true, channel, new URI(address));
        ProxyChannel proxyChannel = new ProxyChannel(manager);
        server.addProxyChannel(address, proxyChannel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        String address = channel.remoteAddress().toString().substring(1);
        GproxyProtocol protocol = (GproxyProtocol) msg;
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        int cmd = header.getCmd();
        switch (cmd) {
            case GproxyCommand.GAME_EVENT:
                String content = body.getContent();
                if ("login".equals(content)) {
                    header.setCmd(GproxyCommand.SEND_TO_ALL);
                    body.setContent("login success");
                    header.setContentLen(body.getContentLen());
                }
                // 向当前proxy发送
                if (server.getProxyChannels().containsKey(address)) {
                    Channel sendChannel = server.getProxyChannels().get(address).getManager().getChannel();
                    sendChannel.writeAndFlush(protocol);
                }
                // 向所有proxy发送
                /*if (server.getProxyChannels().size() > 0) {
                    for (ProxyChannel proxyChannel: server.getProxyChannels().values()) {
                        proxyChannel.getManager().getChannel().writeAndFlush(protocol);
                    }
                }*/
        }
    }
}
