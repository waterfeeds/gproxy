package com.waterfeeds.gproxy.network;

import com.waterfeeds.gproxy.message.URI;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class ChannelManager {
    private boolean available;
    private ChannelFuture channelFuture;
    private Channel channel;
    private URI uri;

    public ChannelManager(boolean available, ChannelFuture channelFuture, URI uri) {
        this.available = available;
        this.channelFuture = channelFuture;
        this.uri = uri;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public Channel getChannel() {
        return channelFuture.channel();
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
