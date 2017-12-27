package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.DefaultServerApiService;
import com.waterfeeds.gproxy.server.base.Callback;
import com.waterfeeds.gproxy.server.handler.ServerChannelInitializer;
import com.waterfeeds.gproxy.zookeeper.Certificate;
import com.waterfeeds.gproxy.zookeeper.ZookeeperService;
import org.apache.zookeeper.CreateMode;

public class Server {
    private BaseServer baseServer;
    private Callback callback;

    public Server(Callback callback) {
        this.callback = callback;
        this.callback.setServer(this);
    }

    public void startServer() {
        DefaultServerApiService serverService = new DefaultServerApiService();
        serverService.setPort(8081);
        BaseServer baseServer = new BaseServer(callback);
        this.baseServer = baseServer;
        ServerChannelInitializer serverInitializer = new ServerChannelInitializer(baseServer);
        serverService.setChannelInitializer(serverInitializer);
        serverService.start();
    }

    public void registerServer() {
        ZookeeperService zookeeperService = new ZookeeperService();
        zookeeperService.setPath("gproxy");
        zookeeperService.setZkAddress("127.0.0.1:2181");
        zookeeperService.setCertificate(new Certificate());
        URI uri = new URI("127.0.0.1", 2181);
        String serverAddress = "127.0.0.1:8081";
        byte[] bytes = serverAddress.getBytes();
        try {
            zookeeperService.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!zookeeperService.exists("/server-01")) {
            zookeeperService.registerNode("/server-01", uri, CreateMode.PERSISTENT, bytes, false);
            System.out.println("register " + zookeeperService.exists("/server-01"));
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

}
