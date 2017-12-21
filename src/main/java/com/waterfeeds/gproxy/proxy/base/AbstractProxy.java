package com.waterfeeds.gproxy.proxy.base;

import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.proxy.channel.ClientChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProxy {
    public ConcurrentHashMap<String, ClientChannel> clientChannels = new ConcurrentHashMap<String, ClientChannel>();
    public ConcurrentHashMap<String, List<ClientChannel>> userChannels = new ConcurrentHashMap<String, List<ClientChannel>>();
    public ConcurrentHashMap<String, List<ClientChannel>> groupChannels = new ConcurrentHashMap<String, List<ClientChannel>>();

    public void sendToAll(GproxyProtocol message) {

    }

    public void sendToClient(String clientId, GproxyProtocol message) {

    }

    public void sendToUser(String userId, GproxyProtocol message) {

    }

    public void sendToGroup(String groupId, GproxyProtocol message) {

    }

    public boolean isOnline(String clientId) {
        return false;
    }

    public boolean isUidOnline(String userId) {
        return false;
    }

    public int getAllClientCount() {
        return 0;
    }

    public int getClientCountByUser(String userId) {
        return 0;
    }

    public int getClientCountByGroup(String groupId) {
        return 0;
    }

    public List<String> getClientIdByUser(String userId) {
        return new ArrayList<String>();
    }

    public boolean closeClient(String clientId) {
        return false;
    }

    public boolean bindUid(String clientId, String userId) {
        return false;
    }

    public boolean unBindUid(String clientId, String userId) {
        return false;
    }

    public boolean joinGroup(String clientId, String groupId) {
        return false;
    }

    public boolean leaveGroup(String clientId, String groupId) {
        return false;
    }
}
