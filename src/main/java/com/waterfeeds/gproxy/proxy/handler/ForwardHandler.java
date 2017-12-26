package com.waterfeeds.gproxy.proxy.handler;

import com.waterfeeds.gproxy.network.ChannelContextFactory;
import com.waterfeeds.gproxy.protocol.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.base.JsonBuf;
import com.waterfeeds.gproxy.proxy.Proxy;
import com.waterfeeds.gproxy.proxy.channel.ServerChannel;
import com.waterfeeds.gproxy.protocol.base.BaseEventConverter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ForwardHandler extends ChannelInboundHandlerAdapter {
    private Proxy proxy;

    public ForwardHandler(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String serverId = ChannelContextFactory.getLongId(ctx);
        ServerChannel serverChannel = ChannelContextFactory.getServerChannel(ctx);
        proxy.addServerChannel(serverId, serverChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String serverId = ChannelContextFactory.getLongId(ctx);
        proxy.removeServerChannel(serverId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GproxyProtocol protocol = (GproxyProtocol) msg;
        String content = protocol.getBody().getContent();
        String message = "";
        int cmd = protocol.getHeader().getCmd();
        switch (cmd) {
            case GproxyCommand.SEND_TO_ALL:
                proxy.sendToAll(protocol);
                break;
            case GproxyCommand.SEND_TO_CLIENT:
                String clientId = JsonBuf.getClientId(content);
                message = JsonBuf.getMessage(content);
                protocol = BaseEventConverter.converter(protocol, message);
                proxy.sendToClient(protocol, clientId);
                break;
            case GproxyCommand.SEND_TO_USER:
                String userId = JsonBuf.getUserId(content);
                message = JsonBuf.getMessage(content);
                protocol = BaseEventConverter.converter(protocol, message);
                proxy.sendToUser(protocol, userId);
                break;
            case GproxyCommand.SEND_TO_GROUP:
                String groupId = JsonBuf.getGroupId(content);
                message = JsonBuf.getMessage(content);
                protocol = BaseEventConverter.converter(protocol, message);
                proxy.sendToGroup(protocol, groupId);
                break;
            case GproxyCommand.GET_CLIENT_COUNT:
                break;
            case GproxyCommand.GET_USER_COUNT:
                break;
            case GproxyCommand.GET_GROUP_COUNT:
                break;
            case GproxyCommand.BIND_UID:

                break;
            case GproxyCommand.UN_BIND_UID:
                break;
            case GproxyCommand.JOIN_GROUP:
                break;
            case GproxyCommand.LEAVE_GROUP:
                break;
        }
    }
}
