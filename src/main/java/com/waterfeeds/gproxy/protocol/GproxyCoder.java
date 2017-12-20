package com.waterfeeds.gproxy.protocol;

import com.waterfeeds.gproxy.protocol.base.AbstractGproxyCoder;
import com.waterfeeds.gproxy.protocol.exception.ProtocolException;
import com.waterfeeds.gproxy.util.ByteUtil;
import org.apache.commons.lang3.ArrayUtils;

public class GproxyCoder implements AbstractGproxyCoder{
    @Override
    public byte[] encode(Object object) {
        GproxyProtocol protocol = (GproxyProtocol) object;
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        byte[] cmd = ByteUtil.int2Byte(header.getCmd(), 1);
        byte[] safe = ByteUtil.int2Byte(header.getSafe(), 1);
        byte[] contentLen = ByteUtil.int2Byte(header.getContentLen(), 2);
        byte[] safeSign = body.getSafeSign().getBytes();
        byte[] content = body.getContent().getBytes();
        byte[] data = ByteUtil.mergeArrays(cmd, safe, contentLen, safeSign, content);
        return data;
    }

    @Override
    public Object decode(byte[] bytes) {
        int cmd = ByteUtil.byte2Int(ArrayUtils.subarray(bytes, 0, 1));
        int safe = ByteUtil.byte2Int(ArrayUtils.subarray(bytes, 1, 2));
        int contentLen = ByteUtil.byte2Int(ArrayUtils.subarray(bytes, 2, 4));
        String safeSign = "";
        String content = "";
        if (contentLen > 0) {
            if ((safe == 0 && bytes.length != contentLen + 4) || (safe == 1 && bytes.length != contentLen + 20)) {
                throw new ProtocolException(10001, "协议格式不正确");
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

    public static void main(String[] args) {
        GproxyProtocol protocol = new GproxyProtocol(new GproxyHeader(1, 1, 5), new GproxyBody("Tdy6gRygBSVJKR1f", "hello"));
        GproxyCoder coder = new GproxyCoder();
        GproxyProtocol receiveProtocol = (GproxyProtocol) coder.decode(coder.encode(protocol));
        GproxyHeader header = receiveProtocol.getHeader();
        GproxyBody body = receiveProtocol.getBody();
        System.out.println(header.getCmd());
        System.out.println(header.getSafe());
        System.out.println(header.getContentLen());
        System.out.println(body.getSafeSign());
        System.out.println(body.getContent());
    }
}
