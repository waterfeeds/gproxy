package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.user.DefaultCallback;

public class ServerApplication2 {
    public static void main(String[] args) {
        DefaultCallback callback = new DefaultCallback();
        Server server = new Server(callback);
        String zkAddress = "127.0.0.1:2181";
        String space = "gproxy";
        String serverName = "/server-02";
        String serverAddress = "127.0.0.1:8082";
        boolean status = server.startServer(8082);
        if (status)
            server.registerServer(zkAddress, space, serverName, serverAddress);
    }

}
