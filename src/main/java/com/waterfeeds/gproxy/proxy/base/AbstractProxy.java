package com.waterfeeds.gproxy.proxy.base;

import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.proxy.channel.ClientChannel;
import sun.nio.cs.ext.MS874;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProxy {
    public ConcurrentHashMap<String, ClientChannel> clientChannels = new ConcurrentHashMap<String, ClientChannel>();
    public ConcurrentHashMap<String, HashMap<String, ClientChannel>> userChannels = new ConcurrentHashMap<String, HashMap<String, ClientChannel>>();
    public ConcurrentHashMap<String, HashMap<String, ClientChannel>> groupChannels = new ConcurrentHashMap<String, HashMap<String, ClientChannel>>();

    public void sendToAll(GproxyProtocol protocol) {
        Iterator iterator = clientChannels.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            ChannelManager manager = ((ClientChannel) entry.getValue()).getManager();
            if (manager.isAvailable()) {
                manager.getChannel().writeAndFlush(protocol);
            }
        }
    }

    public void sendToClient(GproxyProtocol protocol, String clientId) {
        if (clientChannels.containsKey(clientId)) {
            ClientChannel clientChannel = clientChannels.get(clientId);
            ChannelManager manager = clientChannel.getManager();
            if (manager.isAvailable()) {
                manager.getChannel().writeAndFlush(protocol);
            }
        }
    }

    public void sendToUser(GproxyProtocol protocol, String userId) {
        if (userChannels.containsKey(userId)) {
            HashMap<String, ClientChannel> map = userChannels.get(userId);
            sendAll(map, protocol);
        }
    }

    public void sendToGroup(GproxyProtocol protocol, String groupId) {
        if (groupChannels.containsKey(groupId)) {
            HashMap<String, ClientChannel> map = groupChannels.get(groupId);

            sendAll(map, protocol);
        }
    }

    public boolean isOnline(String clientId) {
        if (clientChannels.containsKey(clientId) && clientChannels.get(clientId).getManager().isAvailable()) {
            return true;
        }
        return false;
    }

    public boolean isUidOnline(String userId) {
        HashMap<String, ClientChannel> map = userChannels.get(userId);
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            ClientChannel clientChannel = (ClientChannel) entry.getValue();
            ChannelManager manager = clientChannel.getManager();
            if (manager.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public void addClientChannel(String clientId, ClientChannel clientChannel) {
        clientChannels.put(clientId, clientChannel);
    }

    public void removeClientChannel(String clientId) {
        clientChannels.remove(clientId);
    }

    public int getAllClientCount() {
        return clientChannels.size();
    }

    public int getClientCountByUser(String userId) {
        if (userChannels.containsKey(userId)) {
            return userChannels.get(userId).size();
        }
        return 0;
    }

    public int getClientCountByGroup(String groupId) {
        if (groupChannels.containsKey(groupId)) {
            return groupChannels.get(groupId).size();
        }
        return 0;
    }

    public List<String> getClientIdByUser(String userId) {
        if (userChannels.containsKey(userId)) {
            HashMap<String, ClientChannel> map = userChannels.get(userId);
            return (List<String>) map.keySet();
        }
        return new ArrayList<>(0);
    }

    public List<String> getClientIdByGroup(String groupId) {
        if (groupChannels.containsKey(groupId)) {
            HashMap<String, ClientChannel> map = groupChannels.get(groupId);
            return (List<String>) map.keySet();
        }
        return new ArrayList<>(0);
    }

    public void bindUid(String clientId, String userId) {
        if (clientChannels.containsKey(clientId)) {
            HashMap<String, ClientChannel> map;
            ClientChannel clientChannel = clientChannels.get(clientId);
            if (userChannels.containsKey(userId)) {
                map = userChannels.get(userId);
                map.put(clientId, clientChannel);
                userChannels.put(userId, map);
            } else {
                map = new HashMap<String, ClientChannel>();
                map.put(clientId, clientChannel);
                userChannels.put(userId, map);
            }
        }
    }

    public void unBindUid(String clientId, String userId) {
        if (clientChannels.containsKey(clientId)) {
            HashMap<String, ClientChannel> map = userChannels.get(userId);
            map.remove(clientId);
            userChannels.put(userId, map);
        }
    }

    public void joinGroup(String clientId, String groupId) {
        if (clientChannels.containsKey(clientId)) {
            HashMap<String, ClientChannel> map;
            ClientChannel clientChannel = clientChannels.get(clientId);
            map = groupChannels.get(groupId);
            if (map == null) {
                map = new HashMap<String, ClientChannel>();
            }
            map.put(clientId, clientChannel);
            groupChannels.put(groupId, map);
        }
    }

    public void leaveGroup(String clientId, String groupId) {
        if (groupChannels.containsKey(groupId) && clientChannels.containsKey(clientId)) {
            HashMap<String, ClientChannel> map = groupChannels.get(groupId);
            map.remove(clientId);
            groupChannels.put(groupId, map);
        }
    }

    public void sendAll(HashMap<String, ClientChannel> map, GproxyProtocol protocol) {
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            ClientChannel clientChannel = (ClientChannel) entry.getValue();
            ChannelManager manager = clientChannel.getManager();
            if (manager.isAvailable()) {
                manager.getChannel().writeAndFlush(protocol);
            }
        }
    }
}
