package com.waterfeeds.gproxy.proxy.base;

import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.proxy.channel.ClientChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProxy {
    protected ConcurrentHashMap<String, ClientChannel> clientChannels = new ConcurrentHashMap<String, ClientChannel>();
    protected ConcurrentHashMap<String, List<ClientChannel>> userChannels = new ConcurrentHashMap<String, List<ClientChannel>>();
    protected ConcurrentHashMap<String, List<ClientChannel>> groupChannels = new ConcurrentHashMap<String, List<ClientChannel>>();

    protected void sendToAll(GproxyProtocol message) {

    }

    protected void sendToClient(String clientId, GproxyProtocol message) {

    }

    protected void sendToUser(String userId, GproxyProtocol message) {

    }

    protected void sendToGroup(String groupId, GproxyProtocol message) {

    }

    protected boolean isOnline(String clientId) {
        return false;
    }

    protected boolean isUidOnline(String userId) {
        return false;
    }

    protected int getAllClientCount() {
        return 0;
    }

    protected int getClientCountByUser(String userId) {
        return 0;
    }

    protected int getClientCountByGroup(String groupId) {
        return 0;
    }

    protected List<String> getClientIdByUser(String userId) {
        return new ArrayList<String>();
    }

    protected boolean closeClient(String clientId) {
        return false;
    }

    protected boolean bindUid(String clientId, String userId) {
        return false;
    }

    protected boolean unBindUid(String clientId, String userId) {
        return false;
    }

    protected boolean joinGroup(String clientId, String groupId) {
        return false;
    }

    protected boolean leaveGroup(String clientId, String groupId) {
        return false;
    }
}
