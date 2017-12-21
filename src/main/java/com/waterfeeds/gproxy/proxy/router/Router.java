package com.waterfeeds.gproxy.proxy.router;

import com.waterfeeds.gproxy.proxy.channel.ServerChannel;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Router {
    public void randRoute(String clientId, CopyOnWriteArrayList<ServerChannel> channels, HashMap<String, ServerChannel> map) {
        int length = channels.size();
        int randIndex = new Random().nextInt(length);
        ServerChannel serverChannel = channels.get(randIndex);
        map.put(clientId, serverChannel);
    }

}
