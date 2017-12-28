package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.user.DefaultCallback;

public class ServerApplication3 {
    public static void main(String[] args) {
        DefaultCallback callback = new DefaultCallback();
        Server server = new Server(callback);
        String zkAddress = "127.0.0.1:2181";
        String space = "gproxy";
        String serverName = "/server-03";
        String serverAddress = "127.0.0.1:8083";
        boolean status = server.startServer(8083);
        if (status)
            server.registerServer(zkAddress, space, serverName, serverAddress);
    }

}
