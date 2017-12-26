package com.waterfeeds.gproxy.user.base;

public class DefaultEventContext implements AbstractEventContext {
    @Override
    public String generateUserId() {
        return "u";
    }

    @Override
    public String generateGroupId() {
        return "g";
    }
}
