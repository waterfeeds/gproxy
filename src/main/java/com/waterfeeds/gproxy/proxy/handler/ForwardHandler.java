package com.waterfeeds.gproxy.proxy.handler;

import com.waterfeeds.gproxy.network.base.BaseChannelContext;
import com.waterfeeds.gproxy.protocol.base.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.base.GproxyJson;
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
        String serverId = BaseChannelContext.getLongId(ctx);
        ServerChannel serverChannel = BaseChannelContext.getServerChannel(ctx);
        proxy.addServerChannel(serverId, serverChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String serverId = BaseChannelContext.getLongId(ctx);
        proxy.removeServerChannel(serverId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GproxyProtocol protocol = (GproxyProtocol) msg;
        if (!protocol.isSafe())
            return;
        String content = protocol.getBody().getContent();
        String message = "";
        String clientId = "";
        String userId = "";
        String groupId = "";
        int cmd = protocol.getHeader().getCmd();
        switch (cmd) {
            case GproxyCommand.SEND_TO_ALL:
                proxy.sendToAll(protocol);
                break;
            case GproxyCommand.SEND_TO_CLIENT:
                clientId = GproxyJson.getClientId(content);
                message = GproxyJson.getMessage(content);
                protocol = BaseEventConverter.converter(protocol, message);
                proxy.sendToClient(protocol, clientId);
                break;
            case GproxyCommand.SEND_TO_USER:
                userId = GproxyJson.getUserId(content);
                message = GproxyJson.getMessage(content);
                protocol = BaseEventConverter.converter(protocol, message);
                proxy.sendToUser(protocol, userId);
                break;
            case GproxyCommand.SEND_TO_GROUP:
                groupId = GproxyJson.getGroupId(content);
                message = GproxyJson.getMessage(content);
                protocol = BaseEventConverter.converter(protocol, message);
                proxy.sendToGroup(protocol, groupId);
                break;
            case GproxyCommand.BIND_UID:
                clientId = GproxyJson.getClientId(content);
                userId = GproxyJson.getUserId(content);
                proxy.bindUid(clientId, userId);
                break;
            case GproxyCommand.UN_BIND_UID:
                clientId = GproxyJson.getClientId(content);
                userId = GproxyJson.getUserId(content);
                proxy.unBindUid(clientId, userId);
                break;
            case GproxyCommand.JOIN_GROUP:
                clientId = GproxyJson.getClientId(content);
                groupId = GproxyJson.getGroupId(content);
                proxy.joinGroup(clientId, groupId);
                break;
            case GproxyCommand.LEAVE_GROUP:
                clientId = GproxyJson.getClientId(content);
                groupId = GproxyJson.getGroupId(content);
                proxy.leaveGroup(clientId, groupId);
                break;
            case GproxyCommand.GET_CLIENT_COUNT:
                break;
            case GproxyCommand.GET_USER_COUNT:
                break;
            case GproxyCommand.GET_GROUP_COUNT:
                break;
            default:
                break;
        }
    }
}
