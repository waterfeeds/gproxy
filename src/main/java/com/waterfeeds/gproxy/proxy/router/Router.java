package com.waterfeeds.gproxy.proxy.router;

import com.waterfeeds.gproxy.proxy.channel.ServerChannel;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Router {
    public ServerChannel randRoute(ConcurrentHashMap<String, ServerChannel> serverChannels) {
        int length = serverChannels.size();
        int randIndex = new Random().nextInt(length);
        return serverChannels.get(randIndex);
    }

    public void randRoute(String clientId, ConcurrentHashMap<String, ServerChannel> serverChannels, HashMap<String, ServerChannel> map) {
        int length = serverChannels.size();
        int randIndex = new Random().nextInt(length);
        ServerChannel serverChannel = serverChannels.get(randIndex);
        map.put(clientId, serverChannel);
    }

    public void routeByServer(String clientId, String serverId, ConcurrentHashMap<String, ServerChannel> serverChannels, HashMap<String, ServerChannel> map) {
        if (serverChannels.containsKey(serverId)) {
            ServerChannel serverChannel = serverChannels.get(serverId);
            map.put(clientId, serverChannel);
        }
    }

}
