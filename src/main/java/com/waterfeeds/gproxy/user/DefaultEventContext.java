package com.waterfeeds.gproxy.user;

import com.waterfeeds.gproxy.user.base.AbstractEventContext;

public class DefaultEventContext implements AbstractEventContext {
    @Override
    public String generateUserId() {
        return "user";
    }

    @Override
    public String generateGroupId() {
        return "group";
    }
}
