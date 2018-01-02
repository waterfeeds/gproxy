package com.waterfeeds.gproxy.proxy.handler;

import com.waterfeeds.gproxy.message.Const;
import com.waterfeeds.gproxy.network.base.BaseChannelContext;
import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.base.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.base.GproxyJson;
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
        String clientId = BaseChannelContext.getLongId(ctx);
        ClientChannel clientChannel = BaseChannelContext.getClientChannel(ctx);
        proxy.addClientChannel(clientId, clientChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String clientId = BaseChannelContext.getLongId(ctx);
        proxy.removeClientChannel(clientId);
        proxy.removeRouteChannel(clientId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GproxyProtocol protocol = (GproxyProtocol) msg;
        if (!protocol.isSafe()) {
            return;
        }
        String clientId = BaseChannelContext.getLongId(ctx);
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        String content = body.getContent();
        int cmd = header.getCmd();
        switch (cmd) {
            case GproxyCommand.CLIENT_EVENT:
                int routerMode = proxy.getRouter().getRouterMode();
                if (routerMode == Const.ROUTER_RANDOM) {
                    //随机路由
                    protocol = BaseEventConverter.converterByClientId(protocol, content, clientId);
                    ServerChannel serverChannel = proxy.getRandRouteChannel(clientId);
                    if (serverChannel.isAvailable()) {
                        serverChannel.getChannel().writeAndFlush(protocol);
                    } else {
                        proxy.removeRouterMap(clientId);
                        serverChannel = proxy.getRandRouteChannel(clientId);
                        if (serverChannel.isAvailable()) {
                            serverChannel.getChannel().writeAndFlush(protocol);
                        }
                    }
                } else if (routerMode == Const.ROUTER_ASSIGN) {
                    //根据服务器分配路由
                    protocol = BaseEventConverter.converterByClientId(protocol, content, clientId);
                    String serverName = GproxyJson.getServerId(content);
                    ServerChannel serverChannel = proxy.getRouteChannelByServer(clientId, serverName);
                    if (serverChannel.isAvailable()) {
                        serverChannel.getChannel().writeAndFlush(protocol);
                    }
                }

                break;
            default:
                break;
        }
    }
}
