package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.user.DefaultCallback;

public class ServerApplication {

    public static void main(String[] args) {
        DefaultCallback callback = new DefaultCallback();
        Server server = new Server(callback);
        server.registerServer();
        server.startServer();
    }

}
