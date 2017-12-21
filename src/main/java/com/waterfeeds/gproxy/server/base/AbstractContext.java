package com.waterfeeds.gproxy.server.base;

public interface AbstractContext {
    public String generateClientId();

    public String generateUserId();

    public String generateGroupId();
}
