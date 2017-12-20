package com.waterfeeds.gproxy.protocol.base;

public interface AbstractGproxyCoder {
    public byte[] encode(Object object);
    public Object decode(byte[] bytes);
}
