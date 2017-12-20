package com.waterfeeds.gproxy.protocol.base;

import com.waterfeeds.gproxy.protocol.GproxyProtocol;

public interface AbstractGproxyCoder {
    public byte[] encode(Object object);
    public GproxyProtocol decode(byte[] bytes);
}
