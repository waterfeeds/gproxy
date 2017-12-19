package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.network.DefaultClientApiService;
import com.waterfeeds.gproxy.network.DefaultServerApiService;

public class ServerApplication {
    public static void main(String[] args) {
        final DefaultClientApiService clientProxy = DefaultClientApiService.newInstance(Runtime.getRuntime().availableProcessors() * 2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                URI uri = new URI("127.0.0.1", 8081);
                ChannelManager manager = clientProxy.doConnect(uri);
                manager.getChannel().writeAndFlush("this is server");
            }
        }).start();
        DefaultServerApiService serverProxy = new DefaultServerApiService();
        serverProxy.setPort(8080);
        serverProxy.start();
    }
}
