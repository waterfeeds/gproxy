package com.waterfeeds.gproxy.user;

import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.server.base.Callback;
import com.waterfeeds.gproxy.user.base.AbstractContext;

public class DefaultCallback extends Callback {

    @Override
    public void onMessage(GproxyProtocol protocol) {
        AbstractContext context = new DefaultContext();
        GproxyBody body = protocol.getBody();
        String content = body.getContent();
        String message = getMessageByBody(content);
        String clientId = getClientIdByBody(content);
        String groupId = context.generateGroupId();
        if (message.contains("join group")) {
            joinGroup(clientId, groupId);
        } else if (message.contains("leave group")) {
            leaveGroup(clientId, groupId);
        } else {
            sendToGroup(message, groupId);
        }
    }
}
