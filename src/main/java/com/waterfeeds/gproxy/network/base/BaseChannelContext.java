package com.waterfeeds.gproxy.network.base;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.proxy.channel.ClientChannel;
import com.waterfeeds.gproxy.proxy.channel.ServerChannel;
import com.waterfeeds.gproxy.server.channel.ProxyChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class BaseChannelContext {
    public static String getShortId(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        return channel.id().asShortText();
    }

    public static String getLongId(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        return channel.id().asLongText();
    }

    public static ClientChannel getClientChannel(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String address = channel.remoteAddress().toString().substring(1);
        ChannelManager manager = new ChannelManager(true, channel, new URI(address));
        ClientChannel clientChannel = new ClientChannel(manager);
        return clientChannel;
    }

    public static ServerChannel getServerChannel(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String address = channel.remoteAddress().toString().substring(1);
        ChannelManager manager = new ChannelManager(true, channel, new URI(address));
        ServerChannel serverChannel = new ServerChannel(manager);
        return serverChannel;
    }

    public static ProxyChannel getProxyChannel(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String address = channel.remoteAddress().toString().substring(1);
        ChannelManager manager = new ChannelManager(true, channel, new URI(address));
        ProxyChannel proxyChannel = new ProxyChannel(manager);
        return proxyChannel;
    }
}
