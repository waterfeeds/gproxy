package com.waterfeeds.gproxy.client.handler;

import com.waterfeeds.gproxy.client.Chat;
import com.waterfeeds.gproxy.network.base.BaseChannelInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ChatChannelInitializer extends ChannelInitializer<SocketChannel> {
    private Chat chat;

    public ChatChannelInitializer(Chat chat) {
        this.chat = chat;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch = BaseChannelInitializer.baseInit(ch);
        ch.pipeline().addLast(new ChatHandler(chat));
    }
}
