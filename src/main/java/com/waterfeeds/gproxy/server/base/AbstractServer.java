package com.waterfeeds.gproxy.server.base;

import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;

public class AbstractServer {
    public GproxyProtocol sendToAll(String content) {
        GproxyBody body = new GproxyBody(content);
        GproxyHeader header = new GproxyHeader(GproxyCommand.SEND_TO_ALL, body.getContentLen());
        GproxyProtocol protocol = new GproxyProtocol(header, body);
        return protocol;
    }
}
