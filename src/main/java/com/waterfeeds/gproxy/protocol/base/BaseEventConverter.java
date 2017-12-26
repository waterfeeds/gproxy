package com.waterfeeds.gproxy.protocol.base;

import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;

public class BaseEventConverter {

    public static GproxyProtocol converter(GproxyProtocol protocol, String message, int cmd) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        header.setCmd(cmd);
        body.setContent(message);
        header.setContentLen(body.getContentLen());
        protocol.setHeader(header);
        protocol.setBody(body);
        return protocol;
    }

    public static GproxyProtocol converter(GproxyProtocol protocol, String message) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        body.setContent(message);
        header.setContentLen(body.getContentLen());
        protocol.setHeader(header);
        protocol.setBody(body);
        return protocol;
    }

    public static GproxyProtocol converterByClientId(GproxyProtocol protocol, String message, String clientId) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        String content = JsonBuf.getJsonByClientId(clientId, message);
        body.setContent(content);
        header.setContentLen(body.getContentLen());
        protocol.setHeader(header);
        protocol.setBody(body);
        return protocol;
    }

    public static GproxyProtocol converterByClientId(GproxyProtocol protocol, String message, String clientId, int cmd) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        header.setCmd(cmd);
        String content = JsonBuf.getJsonByClientId(clientId, message);
        body.setContent(content);
        header.setContentLen(body.getContentLen());
        protocol.setHeader(header);
        protocol.setBody(body);
        return protocol;
    }
}
