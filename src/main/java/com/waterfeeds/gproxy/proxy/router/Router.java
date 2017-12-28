package com.waterfeeds.gproxy.proxy.router;

import com.waterfeeds.gproxy.proxy.channel.ServerChannel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Router {
    static Random random = new Random();
    public ServerChannel randRoute(ConcurrentHashMap<String, ServerChannel> serverChannels) {
        int length = serverChannels.size();
        if (length == 0) {
            return null;
        }
        int randIndex = new Random().nextInt(length);
        Iterator iterator = serverChannels.entrySet().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (randIndex == index) {
                ServerChannel serverChannel = (ServerChannel) entry.getValue();
                return serverChannel;
            }
            index++;
        }
        return null;
    }

    public void randRoute(String clientId, ConcurrentHashMap<String, ServerChannel> serverChannels, HashMap<String, ServerChannel> map) {
        int length = serverChannels.size();
        if (length == 0) {
            return;
        }
        int randIndex = random.nextInt(length);
        Iterator iterator = serverChannels.entrySet().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (randIndex == index) {
                ServerChannel serverChannel = (ServerChannel) entry.getValue();
                map.put(clientId, serverChannel);
                break;
            }
            index++;
        }
    }

    public void routeByServer(String clientId, String serverId, ConcurrentHashMap<String, ServerChannel> serverChannels, HashMap<String, ServerChannel> map) {
        if (serverChannels.containsKey(serverId)) {
            ServerChannel serverChannel = serverChannels.get(serverId);
            map.put(clientId, serverChannel);
        }
    }


}
