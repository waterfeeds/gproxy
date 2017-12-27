package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.user.DefaultCallback;

public class ServerApplication {

    public static void main(String[] args) {
        DefaultCallback callback = new DefaultCallback();
        Server server = new Server(callback);
        String zkAddress = "127.0.0.1:2181";
        String zkSpace = "gproxy";
        String zkNodeName = "/server-01";
        String serverAddress = "127.0.0.1:8081";
        server.registerServer(zkAddress, zkSpace, zkNodeName, serverAddress);
        server.startServer(8081);
    }

}
