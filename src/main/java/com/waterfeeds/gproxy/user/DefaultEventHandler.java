package com.waterfeeds.gproxy.user;

import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.base.BaseEventConverter;
import com.waterfeeds.gproxy.protocol.base.JsonBuf;
import com.waterfeeds.gproxy.user.base.AbstractEventContext;
import com.waterfeeds.gproxy.user.base.AbstractEventHandler;

public class DefaultEventHandler implements AbstractEventHandler {
    private AbstractEventContext context;

    public DefaultEventHandler() {
        this.context = new DefaultEventContext();
    }

    public DefaultEventHandler(AbstractEventContext context) {
        this.context = context;
    }

    @Override
    public GproxyProtocol handleEvent(GproxyProtocol protocol, int cmd) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        String content = body.getContent();
        if (header.getCmd() != GproxyCommand.CLIENT_EVENT) {
                return null;
        }
        String clientId = JsonBuf.getClientId(content);
        String message = "receive: " + JsonBuf.getMessage(content);
        switch (cmd) {
            case GproxyCommand.SEND_TO_ALL:
                return BaseEventConverter.converter(message, GproxyCommand.SEND_TO_ALL);
            case GproxyCommand.SEND_TO_CLIENT:
                return BaseEventConverter.converterByClientId(message, clientId, GproxyCommand.SEND_TO_CLIENT);
            case GproxyCommand.SEND_TO_USER:
                return BaseEventConverter.converterByUserId(message, context.generateUserId(), GproxyCommand.SEND_TO_USER);
            case GproxyCommand.SEND_TO_GROUP:
                return BaseEventConverter.converterByGroupId(message, context.generateGroupId(), GproxyCommand.SEND_TO_GROUP);
            case GproxyCommand.BIND_UID:
                return BaseEventConverter.converterByUserId(message, clientId, context.generateUserId(), GproxyCommand.BIND_UID);
            case GproxyCommand.UN_BIND_UID:
                return BaseEventConverter.converterByUserId(message, clientId, context.generateUserId(), GproxyCommand.UN_BIND_UID);
            case GproxyCommand.JOIN_GROUP:
                return BaseEventConverter.converterByGroupId(message, clientId, context.generateGroupId(), GproxyCommand.JOIN_GROUP);
            case GproxyCommand.LEAVE_GROUP:
                return BaseEventConverter.converterByGroupId(message, clientId, context.generateGroupId(), GproxyCommand.LEAVE_GROUP);

        }
        return protocol;
    }
}
