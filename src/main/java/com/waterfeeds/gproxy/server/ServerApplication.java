package com.waterfeeds.gproxy.server;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.DefaultServerApiService;
import com.waterfeeds.gproxy.server.handler.ServerChannelInitializer;
import com.waterfeeds.gproxy.server.handler.ServerHandler;
import com.waterfeeds.gproxy.zookeeper.Certificate;
import com.waterfeeds.gproxy.zookeeper.ZookeeperService;
import org.apache.zookeeper.CreateMode;

public class ServerApplication {
    public static void main(String[] args) throws Exception {
        registerServer();
        startServer();

    }

    public static void startServer() {
        DefaultServerApiService serverProxy = new DefaultServerApiService();
        serverProxy.setPort(8081);
        Server server = new Server();
        ServerHandler serverHandler = new ServerHandler(server);
        ServerChannelInitializer serverInitializer = new ServerChannelInitializer();
        serverInitializer.init(serverHandler);
        serverProxy.setChannelInitializer(serverInitializer);
        serverProxy.start();
    }

    public static void registerServer() {
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

}
