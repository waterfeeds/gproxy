package com.waterfeeds.gproxy.server.base;

import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.base.GproxyJson;
import com.waterfeeds.gproxy.server.Server;

public abstract class Callback {
    private String proxyId;
    protected Server server;
    public abstract void onMessage(GproxyProtocol protocol);

    public void setProxyId(String proxyId) {
        this.proxyId = proxyId;
    }

    public String getProxyId() {
        return this.proxyId;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void sendToAll(String message) {
        server.sendToAll(message);
    }

    public void sendToClient(String message, String clientId) {
        server.sendToClient(message, clientId);
    }

    public void sendToUser(String message, String userId) {
        server.sendToUser(message, userId);
    }

    public void sendToGroup(String message, String groupId) {
        server.sendToGroup(message, groupId);
    }

    public void bindUid(String clientId, String userId) {
        server.bindUid(clientId, userId);
    }


    public void unBindUid(String clientId, String userId) {
        server.unBindUid(clientId, userId);
    }


    public void joinGroup(String clientId, String groupId) {
        server.joinGroup(clientId, groupId);
    }

    public void leaveGroup(String clientId, String groupId) {
        server.leaveGroup(clientId, groupId);
    }

    public String getClientIdByBody(String bodyContent) {
        return server.getClientIdByBody(bodyContent);
    }

    public String getUserIdByBody(String bodyContent) {
        return server.getUserIdByBody(bodyContent);
    }

    public String getGroupIdByBody(String bodyContent) {
        return server.getGroupIdByBody(bodyContent);
    }

    public String getMessageByBody(String bodyContent) {
        return server.getMessageByBody(bodyContent);
    }
}
