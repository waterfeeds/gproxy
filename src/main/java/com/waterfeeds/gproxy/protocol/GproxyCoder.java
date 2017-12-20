package com.waterfeeds.gproxy.protocol;

import com.waterfeeds.gproxy.protocol.base.AbstractGproxyCoder;

public class GproxyCoder implements AbstractGproxyCoder{
    @Override
    public byte[] encode(Object object) {
        GproxyProtocol protocol = (GproxyProtocol) object;
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        int bodyLen = body.getContentLen();
        int protocolLen = bodyLen + 16;
        byte[] bytes = new byte[protocolLen];
        
        return bytes;
    }

    @Override
    public Object decode(byte[] bytes) {
        return null;
    }
}
