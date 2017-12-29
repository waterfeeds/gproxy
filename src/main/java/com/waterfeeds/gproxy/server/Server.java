package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.message.Const;
import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.DefaultServerApiService;
import com.waterfeeds.gproxy.protocol.base.GproxyJson;
import com.waterfeeds.gproxy.server.base.Callback;
import com.waterfeeds.gproxy.server.handler.ServerChannelInitializer;
import com.waterfeeds.gproxy.zookeeper.Certificate;
import com.waterfeeds.gproxy.zookeeper.RemoteAddress;
import com.waterfeeds.gproxy.zookeeper.ZookeeperService;
import org.apache.zookeeper.CreateMode;

public class Server {
    private BaseServer baseServer;
    private Callback callback;

    public Server(Callback callback) {
        this.callback = callback;
        this.callback.setServer(this);
    }

    public boolean startServer(int port) {
        DefaultServerApiService serverService = new DefaultServerApiService();
        serverService.setPort(port);
        BaseServer baseServer = new BaseServer(callback);
        this.baseServer = baseServer;
        ServerChannelInitializer serverInitializer = new ServerChannelInitializer(baseServer);
        serverService.setChannelInitializer(serverInitializer);
        return serverService.start();
    }

    public void registerServer(String zkAddress, String space, String serverName, String serverAddress) {
        ZookeeperService zookeeperService = new ZookeeperService();
        zookeeperService.setPath(space);
        zookeeperService.setZkAddress(zkAddress);
        zookeeperService.setCertificate(new Certificate());
        URI uri = new URI(zkAddress);
        byte[] bytes = serverAddress.getBytes();
        try {
            zookeeperService.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String nameSpace = Const.ZOOKEEPER_NAMESPACE_SERVERS;
        //zookeeperService.removeNode(nameSpace, true);
        if (!zookeeperService.exists(nameSpace)) {
            zookeeperService.registerNode(nameSpace, uri, CreateMode.PERSISTENT, Const.ZOOKEEPER_NAMESPACE_SERVERS.getBytes(), false);
        }
        serverName = nameSpace + serverName;
        if (!zookeeperService.exists(serverName)) {
            zookeeperService.registerNode(serverName, uri, CreateMode.EPHEMERAL, bytes, false);
        }
    }

    public void sendToAll(String message) {
        baseServer.sendToAll(message);
    }

    public void sendToClient(String message, String clientId) {
        String proxyId = callback.getProxyId();
        baseServer.sendToClient(message, clientId, proxyId);
    }

    public void sendToUser(String message, String userId) {
        baseServer.sendToUser(message, userId);
    }

    public void sendToGroup(String message, String groupId) {
        baseServer.sendToGroup(message, groupId);
    }

    public void bindUid(String clientId, String userId) {
        String proxyId = callback.getProxyId();
        baseServer.bindUid(clientId, userId, proxyId);
    }


    public void unBindUid(String clientId, String userId) {
        String proxyId = callback.getProxyId();
        baseServer.unBindUid(clientId, userId, proxyId);
    }


    public void joinGroup(String clientId, String groupId) {
        String proxyId = callback.getProxyId();
        baseServer.joinGroup(clientId, groupId, proxyId);
    }

    public void leaveGroup(String clientId, String groupId) {
        String proxyId = callback.getProxyId();
        baseServer.leaveGroup(clientId, groupId, proxyId);
    }

    public String getClientIdByBody(String bodyContent) {
        return GproxyJson.getClientId(bodyContent);
    }

    public String getUserIdByBody(String bodyContent) {
        return GproxyJson.getUserId(bodyContent);
    }

    public String getGroupIdByBody(String bodyContent) {
        return GproxyJson.getGroupId(bodyContent);
    }

    public String getMessageByBody(String bodyContent) {
        return GproxyJson.getMessage(bodyContent);
    }

    public String getBodyByClientId(String clientId, String message) {
        return GproxyJson.getJsonByClientId(clientId, message);
    }

    public String getBodyByUserId(String userId, String message) {
        return GproxyJson.getJsonByUserId(userId, message);
    }

    public String getBodyByUserId(String clientId, String userId, String message) {
        return GproxyJson.getJsonByUserId(clientId, userId, message);
    }

    public String getBodyByGroupId(String groupId, String message) {
        return GproxyJson.getJsonByGroupId(groupId, message);
    }

    public String getBodyByGroupId(String clientId, String groupId, String message) {
        return GproxyJson.getJsonByGroupId(clientId, groupId, message);
    }

}
