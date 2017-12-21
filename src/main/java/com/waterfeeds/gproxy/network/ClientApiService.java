package com.waterfeeds.gproxy.network;

import com.waterfeeds.gproxy.message.URI;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public abstract class ClientApiService {
    protected static final Logger log = LoggerFactory.getLogger(ClientApiService.class.getSimpleName());

    protected static final ConcurrentHashMap<String, ChannelManager> channels = new ConcurrentHashMap<String, ChannelManager>();

    public static String getRemoteStr(URI uri) {
        if (uri != null) {
            return uri.getHost() + ":" + uri.getPort();
        }
        return null;
    }

    public abstract ChannelManager doConnect(URI uri);

    protected void sendMessage(ChannelFuture channelFuture, Object msg) {
        channelFuture.channel().writeAndFlush(msg);
    }


}
