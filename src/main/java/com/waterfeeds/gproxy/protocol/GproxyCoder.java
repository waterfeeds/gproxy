package com.waterfeeds.gproxy.protocol;

import com.waterfeeds.gproxy.protocol.base.AbstractGproxyCoder;
import com.waterfeeds.gproxy.protocol.base.GproxyErrorCode;
import com.waterfeeds.gproxy.protocol.exception.ProtocolException;
import com.waterfeeds.gproxy.util.ByteUtil;
import org.apache.commons.lang3.ArrayUtils;

public class GproxyCoder implements AbstractGproxyCoder {
    @Override
    public byte[] encode(GproxyProtocol protocol) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        byte[] identifier = ByteUtil.int2Byte(protocol.getIdentifier(), 1);
        byte[] length = ByteUtil.int2Byte(protocol.getLength(), 2);
        byte[] cmd = ByteUtil.int2Byte(header.getCmd(), 1);
        byte[] safe = ByteUtil.int2Byte(header.getSafe(), 1);
        byte[] contentLen = ByteUtil.int2Byte(header.getContentLen(), 2);
        byte[] safeSign = body.getSafeSign().getBytes();
        byte[] content = body.getContent().getBytes();
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
            if (safe == 0) {
                content = new String(ArrayUtils.subarray(bytes, 4, bytes.length));
            } else {
                safeSign = new String(ArrayUtils.subarray(bytes, 4, 20));
                content = new String(ArrayUtils.subarray(bytes, 20, bytes.length));
            }
        }
        GproxyHeader header = new GproxyHeader(cmd, safe, contentLen);
        GproxyBody body = new GproxyBody(safeSign, content);
        return new GproxyProtocol(header, body);
    }

}
