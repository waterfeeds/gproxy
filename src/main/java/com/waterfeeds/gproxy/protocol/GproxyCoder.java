package com.waterfeeds.gproxy.protocol;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.waterfeeds.gproxy.protocol.base.AbstractGproxyCoder;
import com.waterfeeds.gproxy.protocol.base.GproxyErrorCode;
import com.waterfeeds.gproxy.protocol.exception.ProtocolException;
import com.waterfeeds.gproxy.util.ByteUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class GproxyCoder implements AbstractGproxyCoder {
    private GproxyCoder() {

    }

    private static class GproxyCoderHolder {
        private static GproxyCoder instance = new GproxyCoder();
    }

    public static GproxyCoder getInstance() {
        return GproxyCoderHolder.instance;
    }

    @Override
    public byte[] encode(GproxyProtocol protocol) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        byte[] identifier = ByteUtil.int2Byte(protocol.getIdentifier(), 1);
        byte[] cmd = ByteUtil.int2Byte(header.getCmd(), 1);
        byte[] safe = ByteUtil.int2Byte(header.getSafe(), 1);
        byte[] safeSign = body.getSafeSign().getBytes();
        byte[] content = body.getContent().getBytes();
        byte[] contentLen = ByteUtil.int2Byte(content.length, 2);
        byte[] length = null;
        if (header.getSafe() == 0) {
            length = ByteUtil.int2Byte(content.length + 4, 2);
        } else {
            length = ByteUtil.int2Byte(content.length + 20, 2);
        }
        byte[] data = ByteUtil.mergeArrays(identifier, length, cmd, safe, contentLen, safeSign, content);
        return data;
    }

    @Override
    public GproxyProtocol decode(byte[] bytes) {
        int cmd = ByteUtil.byte2Int(ArrayUtils.subarray(bytes, 0, 1));
        int safe = ByteUtil.byte2Int(ArrayUtils.subarray(bytes, 1, 2));
        int contentLen = ByteUtil.byte2Int(ArrayUtils.subarray(bytes, 2, 4));
        String safeSign = "";
        String content = "";
        if (contentLen > 0) {
            if ((safe == 0 && bytes.length != contentLen + 4) || (safe == 1 && bytes.length != contentLen + 20)) {
                throw new ProtocolException(GproxyErrorCode.BAD_PROTOCOL_CODE, GproxyErrorCode.BAD_PROTOCOL_MESSAGE);
            }
            try {
                if (safe == 0) {
                    content = new String(ArrayUtils.subarray(bytes, 4, bytes.length), "UTF-8");
                } else {
                    safeSign = new String(ArrayUtils.subarray(bytes, 4, 20));
                    content = new String(ArrayUtils.subarray(bytes, 20, bytes.length), "UTF-8");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        GproxyHeader header = new GproxyHeader(cmd, safe, contentLen);
        GproxyBody body = new GproxyBody(safeSign, content);
        return new GproxyProtocol(header, body);
    }

    public static void main(String[] args) {
        byte[] content = null;
        try {
            int cmd = 101;
            content = ByteUtil.mergeArrays(ByteUtil.int2Byte(cmd, 1), "高凯".getBytes("UTF-8"));
            String data = new String(ArrayUtils.subarray(content, 1, content.length), "UTF-8");
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
