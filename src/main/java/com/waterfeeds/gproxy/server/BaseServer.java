package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.protocol.base.GproxyCommand;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.base.BaseEventConverter;
import com.waterfeeds.gproxy.server.base.Callback;
import com.waterfeeds.gproxy.server.channel.ProxyChannel;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseServer {
    private String id;
    private String name;
    private URI uri;
    private Callback callback;
    private ConcurrentHashMap<String, ProxyChannel> proxyChannels;

    public BaseServer(Callback callback) {
        this.callback = callback;
        initProxyChannels();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public ConcurrentHashMap<String, ProxyChannel> getProxyChannels() {
        return proxyChannels;
    }

    public void setProxyChannels(ConcurrentHashMap<String, ProxyChannel> proxyChannels) {
        this.proxyChannels = proxyChannels;
    }

    public void addProxyChannel(String proxyId, ProxyChannel proxyChannel) {
        proxyChannels.put(proxyId, proxyChannel);
    }

    public void removeProxyChannel(String proxyId) {
        proxyChannels.remove(proxyId);
    }

    public void initProxyChannels() {
        proxyChannels = new ConcurrentHashMap<String, ProxyChannel>();
    }

    public void sendToAll(String message) {
        GproxyProtocol protocol = BaseEventConverter.converter(message, GproxyCommand.SEND_TO_ALL);
        send(protocol, "");
    }

    public void sendToClient(String message, String clientId, String proxyId) {
        GproxyProtocol protocol = BaseEventConverter.converterByClientId(message, clientId, GproxyCommand.SEND_TO_CLIENT);
        send(protocol, proxyId);
    }

    public void sendToUser(String message, String userId) {
        GproxyProtocol protocol = BaseEventConverter.converterByUserId(message, userId, GproxyCommand.SEND_TO_USER);
        send(protocol, "");
    }

    public void sendToGroup(String message, String groupId) {
        GproxyProtocol protocol = BaseEventConverter.converterByGroupId(message, groupId, GproxyCommand.SEND_TO_GROUP);
        send(protocol, "");
    }

    public void bindUid(String clientId, String userId, String proxyId) {
        GproxyProtocol protocol = BaseEventConverter.converterByUserId("", clientId, userId, GproxyCommand.BIND_UID);
        send(protocol, proxyId);
    }

    public void unBindUid(String clientId, String userId, String proxyId) {
        GproxyProtocol protocol = BaseEventConverter.converterByUserId("", clientId, userId, GproxyCommand.UN_BIND_UID);
        send(protocol, proxyId);
    }

    public void joinGroup(String clientId, String groupId, String proxyId) {
        GproxyProtocol protocol = BaseEventConverter.converterByGroupId("", clientId, groupId, GproxyCommand.JOIN_GROUP);
        send(protocol, proxyId);
    }

    public void leaveGroup(String clientId, String groupId, String proxyId) {
        GproxyProtocol protocol = BaseEventConverter.converterByGroupId("", clientId, groupId, GproxyCommand.LEAVE_GROUP);
        send(protocol, proxyId);
    }

    public void send(GproxyProtocol protocol, String proxyId) {
        if (!StringUtils.isBlank(proxyId)) {
            System.out.println(proxyChannels);
            if (proxyChannels.containsKey(proxyId)) {
                ProxyChannel proxyChannel = proxyChannels.get(proxyId);
                if (proxyChannel.isAvailable()) {
                    proxyChannel.getChannel().writeAndFlush(protocol);
                }
            }
        }else {
            Iterator iterator = proxyChannels.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                ProxyChannel proxyChannel = ((ProxyChannel) entry.getValue());
                if (proxyChannel.isAvailable()) {
                    proxyChannel.getChannel().writeAndFlush(protocol);
                }
            }
        }

    }
}
