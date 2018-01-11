package com.waterfeeds.gproxy.client;

import com.waterfeeds.gproxy.client.handler.ChatChannelInitializer;
import com.waterfeeds.gproxy.client.handler.ClientChannelInitializer;
import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.network.DefaultClientApiService;
import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.base.GproxyCommand;
import com.waterfeeds.gproxy.user.Properties;

public class ChatApplication {
    public static void main(String[] args) {
        Chat chat = new Chat();
        DefaultClientApiService clientApiService = DefaultClientApiService.newInstance(2);
        clientApiService.setChannelInitializer(new ChatChannelInitializer(chat));
        clientApiService.start();
        URI uri = new URI(Properties.PROXY_ONE_ADDRESS);
        ChannelManager manager = clientApiService.doConnect(uri);
        if (manager.isAvailable()) {
            chat.setManager(manager);
            GproxyBody body = new GproxyBody("join group");
            GproxyHeader header = new GproxyHeader(GproxyCommand.CLIENT_EVENT, 0, body.getContentLen());
            GproxyProtocol protocol = new GproxyProtocol(header, body);
            chat.getManager().getChannel().writeAndFlush(protocol);
            chat.showChatUI();
        }
    }
}
