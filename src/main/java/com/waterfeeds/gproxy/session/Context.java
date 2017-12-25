package com.waterfeeds.gproxy.session;

public class Context {
    private String clientId;
    private String userId;
    private String groupId;

    public Context() {

    }

    public Context(String clientId) {
        this.clientId = clientId;
    }

    public Context(String clientId, String userId) {
        this.clientId = clientId;
        this.userId = userId;
    }

    public Context(String clientId, String userId, String groupId) {
        this.clientId = clientId;
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
