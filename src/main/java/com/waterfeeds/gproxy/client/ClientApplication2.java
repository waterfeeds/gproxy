package com.waterfeeds.gproxy.client;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.network.DefaultClientApiService;
import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.base.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;

import java.util.Scanner;

public class ClientApplication2 {
    public static void main(String[] args) {
        startClient();
    }

    public static void startClient() {
        DefaultClientApiService clientApiService = DefaultClientApiService.newInstance(4);
        clientApiService.setChannelInitializer(new ClientChannelInitializer());
        clientApiService.start();
        URI uri = new URI("127.0.0.1", 8079);
        ChannelManager manager = clientApiService.doConnect(uri);
        GproxyBody body = new GproxyBody("login_2");
        GproxyHeader header = new GproxyHeader(GproxyCommand.CLIENT_EVENT, 0, body.getContentLen());
        GproxyProtocol protocol = new GproxyProtocol(header, body);
        Scanner scanner = new Scanner(System.in);
        while (manager.isAvailable()) {
            String content = scanner.nextLine();
            body.setContent(content);
            header.setContentLen(body.getContentLen());
            protocol.setBody(body);
            protocol.setHeader(header);
            manager.getChannel().writeAndFlush(protocol);
        }
    }

}
