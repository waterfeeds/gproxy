package com.waterfeeds.gproxy.client;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.network.DefaultClientApiService;
import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;

public class ClientApplication {
    public static void main(String[] args) {
        startClient();
    }

    public static void startClient() {
        DefaultClientApiService clientApiService = DefaultClientApiService.newInstance(4);
        clientApiService.setChannelInitializer(new ClientChannelInitializer());
        clientApiService.start();
        URI uri = new URI("127.0.0.1", 8080);
        ChannelManager manager = clientApiService.doConnect(uri);
        GproxyBody body = new GproxyBody("login");
        GproxyProtocol protocol = new GproxyProtocol(new GproxyHeader(GproxyCommand.SERVER_EVENT, 0, body.getContentLen()), body);
        if (manager.isAvailable()) {
            manager.getChannel().writeAndFlush(protocol);
        }
    }

}
