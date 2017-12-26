package com.waterfeeds.gproxy.server.base;

import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.base.JsonBuf;

public class BaseEventConverter {
    public static GproxyProtocol sendToAll(GproxyProtocol protocol, String message) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        header.setCmd(GproxyCommand.SEND_TO_ALL);
        body.setContent(message);
        header.setContentLen(body.getContentLen());
        protocol.setHeader(header);
        protocol.setBody(body);
        return protocol;
    }

    public static GproxyProtocol send(GproxyProtocol protocol, String message) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        header.setCmd(GproxyCommand.SERVER_EVENT);
        body.setContent(message);
        header.setContentLen(body.getContentLen());
        protocol.setHeader(header);
        protocol.setBody(body);
        return protocol;
    }

    public static GproxyProtocol sendToClient(GproxyProtocol protocol, String clientId, String message) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        header.setCmd(GproxyCommand.SEND_TO_CLIENT);
        String content = JsonBuf.getJsonByClientId(clientId, message);
        body.setContent(content);
        header.setContentLen(body.getContentLen());
        protocol.setHeader(header);
        protocol.setBody(body);
        return protocol;
    }
}
