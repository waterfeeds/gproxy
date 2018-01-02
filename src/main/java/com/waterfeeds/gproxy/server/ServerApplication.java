package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.user.DefaultCallback;
import com.waterfeeds.gproxy.user.Properties;

public class ServerApplication {
    public static void main(String[] args) {
        DefaultCallback callback = new DefaultCallback();
        Server server = new Server(callback);
        boolean status = server.startServer(Properties.SERVER_ONE_PORT);
        if (status)
            server.registerServer(Properties.getZkAddress(), Properties.SPACE, Properties.SERVER_ONE_NAME, Properties.SERVER_ONE_ADDRESS);
    }
}
