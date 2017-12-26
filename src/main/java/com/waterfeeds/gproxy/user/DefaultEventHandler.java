package com.waterfeeds.gproxy.user;

import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.base.JsonBuf;
import com.waterfeeds.gproxy.server.base.BaseEventConverter;
import com.waterfeeds.gproxy.user.base.AbstractEventHandler;

public class DefaultEventHandler implements AbstractEventHandler{
    @Override
    public GproxyProtocol handleEvent(GproxyProtocol protocol) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        String content = body.getContent();
        int cmd = header.getCmd();
        switch (cmd) {
            case GproxyCommand.CLIENT_EVENT:
                String clientId = JsonBuf.getClientId(content);
                String message = JsonBuf.getMessage(content);
                return BaseEventConverter.sendToClient(protocol, clientId, message);
            default:
                break;
        }
        return null;
    }
}
