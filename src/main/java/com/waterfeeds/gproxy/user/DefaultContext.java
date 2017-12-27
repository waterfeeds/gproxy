package com.waterfeeds.gproxy.user;

import com.waterfeeds.gproxy.user.base.AbstractContext;

public class DefaultContext implements AbstractContext {
    @Override
    public String generateUserId() {
        return "user";
    }

    @Override
    public String generateGroupId() {
        return "group";
    }
}
