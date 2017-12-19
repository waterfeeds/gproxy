package com.waterfeeds.gproxy.register;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.network.DefaultClientApiService;
import com.waterfeeds.gproxy.network.DefaultServerApiService;

public class RegisterApplication {
    public static void main(String[] args) {
        DefaultServerApiService serverProxy = new DefaultServerApiService();
        serverProxy.setPort(8081);
        serverProxy.start();
    }
}
