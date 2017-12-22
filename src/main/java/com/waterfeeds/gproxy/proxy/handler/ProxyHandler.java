package com.waterfeeds.gproxy.proxy.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.proxy.Proxy;
import com.waterfeeds.gproxy.proxy.channel.ServerChannel;
import com.waterfeeds.gproxy.proxy.router.Router;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProxyHandler extends ChannelInboundHandlerAdapter {
    private Proxy proxy;

    public ProxyHandler(Proxy proxy) {
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
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        int cmd = header.getCmd();
        switch (cmd) {
            case GproxyCommand.GAME_EVENT:
                ServerChannel serverChannel = proxy.getRouter().randRoute(proxy.getServerChannels());
                if (serverChannel.getManager().isAvailable()) {
                    serverChannel.getManager().getChannel().writeAndFlush(msg);
                }
        }
    }
}
