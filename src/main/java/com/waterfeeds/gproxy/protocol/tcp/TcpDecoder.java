package com.waterfeeds.gproxy.protocol.tcp;

import com.waterfeeds.gproxy.message.Const;
import com.waterfeeds.gproxy.protocol.GproxyCoder;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class TcpDecoder extends ByteToMessageDecoder {
    public final int BASE_LENGTH = 5;
    public final int MAX_LENGTH = 8192;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() > BASE_LENGTH) {

            // 设置接收包大小上限，防止socket字节流攻击
            if (byteBuf.readableBytes() > MAX_LENGTH) {
                byteBuf.skipBytes(byteBuf.readableBytes());
            }

            int beginReader;
            while (true) {
                beginReader = byteBuf.readerIndex();
                byteBuf.markReaderIndex();
                int readByte = ByteUtil.byte2Int(byteBuf.readByte());
                // 读到了协议的开始标志，跳出while循环
                if (readByte == Const.HEAD_DATA) {
                    break;
                }

                byteBuf.resetReaderIndex();
                byteBuf.readByte();
                if (byteBuf.readableBytes() < BASE_LENGTH) {
                    return;
                }
            }

            int length = byteBuf.readShort();
            if (byteBuf.readableBytes() < length) {
                byteBuf.readerIndex(beginReader);
                return;
            }
            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            GproxyProtocol protocol = new GproxyCoder().decode(bytes);
            list.add(protocol);
        }
    }
}
