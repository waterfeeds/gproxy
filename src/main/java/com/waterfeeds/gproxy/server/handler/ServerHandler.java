package com.waterfeeds.gproxy.server.handler;

import com.waterfeeds.gproxy.network.ChannelContextFactory;
import com.waterfeeds.gproxy.protocol.base.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.server.BaseServer;
import com.waterfeeds.gproxy.server.base.Callback;
import com.waterfeeds.gproxy.server.channel.ProxyChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private BaseServer baseServer;

    public ServerHandler(BaseServer baseServer) {
        this.baseServer = baseServer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String proxyId = ChannelContextFactory.getLongId(ctx);
        ProxyChannel proxyChannel = ChannelContextFactory.getProxyChannel(ctx);
        baseServer.addProxyChannel(proxyId, proxyChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String proxyId = ChannelContextFactory.getLongId(ctx);
        baseServer.removeProxyChannel(proxyId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GproxyProtocol protocol = (GproxyProtocol) msg;
        if (!protocol.isSafe())
            return;
        String proxyId = ChannelContextFactory.getLongId(ctx);
        int cmd = protocol.getHeader().getCmd();
        if (cmd != GproxyCommand.CLIENT_EVENT)
            return;
        Callback callback = baseServer.getCallback();
        callback.setProxyId(proxyId);
        callback.onMessage(protocol);
    }


}
