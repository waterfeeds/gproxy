package com.waterfeeds.gproxy.user;

import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.base.JsonBuf;
import com.waterfeeds.gproxy.server.base.Callback;
import com.waterfeeds.gproxy.user.base.AbstractContext;

public class DefaultCallback extends Callback {

    @Override
    public void onMessage(GproxyProtocol protocol) {
        AbstractContext context = new DefaultContext();
        GproxyBody body = protocol.getBody();
        String content = body.getContent();
        String message = JsonBuf.getMessage(content);
        String clientId = JsonBuf.getClientId(content);
        String groupId = context.generateGroupId();
        if (message.contains("join group")) {
            server.joinGroup(clientId, groupId);
        } else if (message.contains("leave group")) {
            server.leaveGroup(clientId, groupId);
        } else if (message.contains("group")) {
            server.sendToGroup(message, groupId);
        } else {
            server.sendToClient(message, clientId);
        }

    }
}
