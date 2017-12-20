package com.waterfeeds.gproxy.protocol;

import com.waterfeeds.gproxy.protocol.base.AbstractGproxyCoder;
import com.waterfeeds.gproxy.util.Bit;
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
        return ArrayUtils.addAll((byte[]) ArrayUtils.addAll(cmd, safe), contentLen);
    }

    @Override
    public Object decode(byte[] bytes) {
        int cmd = ByteUtil.byte2Int(ArrayUtils.subarray(bytes, 0, 1));
        int safe = ByteUtil.byte2Int(ArrayUtils.subarray(bytes, 1, 2));
        int contentLen = ByteUtil.byte2Int(ArrayUtils.subarray(bytes, 2, bytes.length));
        GproxyHeader header = new GproxyHeader(cmd, safe, contentLen);
        return header;
    }

    public static void main(String[] args) {
        GproxyProtocol protocol = new GproxyProtocol(new GproxyHeader(1, 0, 1000), new GproxyBody("hello"));
        GproxyCoder coder = new GproxyCoder();
        GproxyHeader header = (GproxyHeader) coder.decode(coder.encode(protocol));
        System.out.println(header.getCmd());
        System.out.println(header.getSafe());
        System.out.println(header.getContentLen());
    }
}
