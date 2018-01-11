package com.waterfeeds.gproxy.client.handler;

import com.waterfeeds.gproxy.client.Chat;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChatHandler extends ChannelInboundHandlerAdapter {
    private Chat chat;

    public ChatHandler(Chat chat) {
        this.chat = chat;
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
        chat.getButtonListener().receiveMessage((GproxyProtocol) msg);
    }
}
