package com.waterfeeds.gproxy.proxy;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.DefaultClientApiService;
import com.waterfeeds.gproxy.network.DefaultServerApiService;
import com.waterfeeds.gproxy.proxy.handler.ForwardChannelInitializer;
import com.waterfeeds.gproxy.proxy.handler.ForwardHandler;
import com.waterfeeds.gproxy.proxy.handler.ProxyChannelInitializer;
import com.waterfeeds.gproxy.proxy.handler.ProxyHandler;
import com.waterfeeds.gproxy.zookeeper.Certificate;
import com.waterfeeds.gproxy.zookeeper.ZookeeperService;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;

public class ProxyApplication {
    public static void main(String[] args) {
        registerProxy();
        startProxy();
    }

    public static void startProxy() {
        DefaultServerApiService serverProxy = new DefaultServerApiService();
        serverProxy.setPort(8080);
        Proxy proxy = new Proxy();
        ProxyChannelInitializer proxyInitializer = new ProxyChannelInitializer(proxy);
        serverProxy.setChannelInitializer(proxyInitializer);
        DefaultClientApiService clientApiService = DefaultClientApiService.newInstance(4);
        ForwardChannelInitializer forwardInitializer = new ForwardChannelInitializer(proxy);
        clientApiService.setChannelInitializer(forwardInitializer);
        proxy.setClientApiService(clientApiService);
        URI uri = new URI();
        ZookeeperService zookeeperService = new ZookeeperService();
        zookeeperService.setPath("gproxy");
        zookeeperService.setZkAddress("127.0.0.1:2181");
        zookeeperService.setCertificate(new Certificate());
        try {
            zookeeperService.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String serverId = "/server-01";
        String address = "";
        if (zookeeperService.exists(serverId)) {
            address = zookeeperService.getData(serverId);
        }
        if (!StringUtils.isBlank(address) && uri.parseAddress(address)) {
            proxy.addServerAddress(serverId, uri);
        }
        serverProxy.start();
    }

    public static void registerProxy() {
        ZookeeperService zookeeperService = new ZookeeperService();
        zookeeperService.setPath("gproxy");
        zookeeperService.setZkAddress("127.0.0.1:2181");
        zookeeperService.setCertificate(new Certificate());
        URI uri = new URI("127.0.0.1", 2181);
        String proxyAddress = "127.0.0.1:8080";
        byte[] bytes = proxyAddress.getBytes();
        try {
            zookeeperService.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (zookeeperService.exists("/proxy-01")) {
            zookeeperService.registerNode("/proxy-01", uri, CreateMode.PERSISTENT, bytes, false);
            System.out.println("register " + zookeeperService.exists("/proxy-01"));
        }

    }
}
