package com.waterfeeds.gproxy.protocol.tcp;

import com.waterfeeds.gproxy.protocol.GproxyCoder;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TcpEncoder extends MessageToByteEncoder <GproxyProtocol>{
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, GproxyProtocol gproxyProtocol, ByteBuf byteBuf) throws Exception {
        byte[] bytes = new GproxyCoder().encode(gproxyProtocol);
        byteBuf.writeBytes(bytes);
    }
}
