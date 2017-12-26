package com.waterfeeds.gproxy.user.base;

import com.waterfeeds.gproxy.protocol.GproxyProtocol;

public interface AbstractEventHandler {
    public GproxyProtocol handleEvent(GproxyProtocol protocol, int cmd);
}
