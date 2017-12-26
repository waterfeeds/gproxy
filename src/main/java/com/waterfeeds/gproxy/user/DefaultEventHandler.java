package com.waterfeeds.gproxy.user;

import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.user.base.AbstractEventHandler;

public class DefaultEventHandler implements AbstractEventHandler{
    @Override
    public GproxyProtocol handleEvent(GproxyProtocol protocol) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        int cmd = header.getCmd();
        switch (cmd) {
            case GproxyCommand.SERVER_EVENT:
                header.setCmd(GproxyCommand.SEND_TO_ALL);
                body.setContent("receive: " + body.getContent());
                header.setContentLen(body.getContentLen());
                return protocol;
            default:
                break;
        }
        return null;
    }
}
