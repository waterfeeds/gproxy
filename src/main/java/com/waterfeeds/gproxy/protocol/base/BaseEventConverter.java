package com.waterfeeds.gproxy.protocol.base;

import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;

public class BaseEventConverter {
    public static GproxyProtocol converter(GproxyProtocol protocol, String message) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        body.setContent(message);
        header.setContentLen(body.getContentLen());
        protocol.setHeader(header);
        protocol.setBody(body);
        return protocol;
    }

    public static GproxyProtocol converter(String message, int cmd) {
        GproxyBody body = new GproxyBody(message);
        GproxyHeader header = new GproxyHeader(cmd, body.getContentLen());
        return new GproxyProtocol(header, body);
    }

    public static GproxyProtocol converterByClientId(GproxyProtocol protocol, String message, String clientId) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        String content = GproxyJson.getJsonByClientId(clientId, message);
        body.setContent(content);
        header.setContentLen(body.getContentLen());
        protocol.setHeader(header);
        protocol.setBody(body);
        return protocol;
    }

    public static GproxyProtocol converterByClientId(String message, String clientId, int cmd) {
        String content = GproxyJson.getJsonByClientId(clientId, message);
        GproxyBody body = new GproxyBody(content);
        GproxyHeader header = new GproxyHeader(cmd, body.getContentLen());
        return new GproxyProtocol(header, body);
    }

    public static GproxyProtocol converterByUserId(GproxyProtocol protocol, String message, String userId) {
        GproxyBody body = protocol.getBody();
        GproxyHeader header = protocol.getHeader();
        String content = GproxyJson.getJsonByUserId(userId, message);
        body.setContent(content);
        header.setContentLen(body.getContentLen());
        protocol.setBody(body);
        protocol.setHeader(header);
        return protocol;
    }

    public static GproxyProtocol converterByUserId(GproxyProtocol protocol, String message, String clientId, String userId) {
        GproxyBody body = protocol.getBody();
        GproxyHeader header = protocol.getHeader();
        String content = GproxyJson.getJsonByUserId(clientId, userId, message);
        body.setContent(content);
        header.setContentLen(body.getContentLen());
        protocol.setBody(body);
        protocol.setHeader(header);
        return protocol;
    }

    public static GproxyProtocol converterByUserId(String message, String userId, int cmd) {
        String content = GproxyJson.getJsonByUserId(userId, message);
        GproxyBody body = new GproxyBody(content);
        GproxyHeader header = new GproxyHeader(cmd, body.getContentLen());
        return new GproxyProtocol(header, body);
    }

    public static GproxyProtocol converterByUserId(String message, String clientId, String userId, int cmd) {
        String content = GproxyJson.getJsonByUserId(clientId, userId, message);
        GproxyBody body = new GproxyBody(content);
        GproxyHeader header = new GproxyHeader(cmd, body.getContentLen());
        return new GproxyProtocol(header, body);
    }

    public static GproxyProtocol converterByGroupId(GproxyProtocol protocol, String message, String groupId) {
        String content = GproxyJson.getJsonByGroupId(groupId, message);
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        body.setContent(content);
        header.setContentLen(body.getContentLen());
        protocol.setHeader(header);
        protocol.setBody(body);
        return protocol;
    }

    public static GproxyProtocol converterByGroupId(GproxyProtocol protocol, String message, String clientId, String groupId) {
        String content = GproxyJson.getJsonByGroupId(clientId, groupId, message);
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        body.setContent(content);
        header.setContentLen(body.getContentLen());
        protocol.setHeader(header);
        protocol.setBody(body);
        return protocol;
    }

    public static GproxyProtocol converterByGroupId(String message, String groupId, int cmd) {
        String content = GproxyJson.getJsonByGroupId(groupId, message);
        GproxyBody body = new GproxyBody(content);
        GproxyHeader header = new GproxyHeader(cmd, body.getContentLen());
        return new GproxyProtocol(header, body);
    }

    public static GproxyProtocol converterByGroupId(String message, String clientId, String groupId, int cmd) {
        String content = GproxyJson.getJsonByGroupId(clientId, groupId, message);
        GproxyBody body = new GproxyBody(content);
        GproxyHeader header = new GproxyHeader(cmd, body.getContentLen());
        return new GproxyProtocol(header, body);
    }
}
