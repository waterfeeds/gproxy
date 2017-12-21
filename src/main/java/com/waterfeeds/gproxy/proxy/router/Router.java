package com.waterfeeds.gproxy.proxy.router;

import com.waterfeeds.gproxy.proxy.channel.ServerChannel;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Router {
    public void randRoute(String clientId, CopyOnWriteArrayList<ServerChannel> channels, HashMap<String, ServerChannel> map) {
        int length = channels.size();
        int randIndex = new Random().nextInt(length);
        ServerChannel serverChannel = channels.get(randIndex);
        map.put(clientId, serverChannel);
    }

    public void routeByServer(String clientId, String serverId, ConcurrentHashMap<String, ServerChannel> serverChannels, HashMap<String, ServerChannel> map) {
        if (serverChannels.containsKey(serverId)) {
            ServerChannel serverChannel = serverChannels.get(serverId);
            map.put(clientId, serverChannel);
        }
    }

}
