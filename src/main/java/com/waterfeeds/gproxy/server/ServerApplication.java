package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.user.DefaultCallback;

public class ServerApplication {
    public static void main(String[] args) {
        DefaultCallback callback = new DefaultCallback();
        Server server = new Server(callback);
        String zkAddress = "127.0.0.1:2181";
        String space = "gproxy";
        String serverName = "/server-01";
        String serverAddress = "127.0.0.1:8081";
        boolean status = server.startServer(8081);
        if (status)
            server.registerServer(zkAddress, space, serverName, serverAddress);
    }

}
