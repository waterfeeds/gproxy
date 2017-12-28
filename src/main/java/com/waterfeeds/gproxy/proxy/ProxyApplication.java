package com.waterfeeds.gproxy.proxy;

import com.waterfeeds.gproxy.message.Const;
import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.DefaultClientApiService;
import com.waterfeeds.gproxy.network.DefaultServerApiService;
import com.waterfeeds.gproxy.proxy.handler.ForwardChannelInitializer;
import com.waterfeeds.gproxy.proxy.handler.ForwardHandler;
import com.waterfeeds.gproxy.proxy.handler.ProxyChannelInitializer;
import com.waterfeeds.gproxy.proxy.handler.ProxyHandler;
import com.waterfeeds.gproxy.zookeeper.Certificate;
import com.waterfeeds.gproxy.zookeeper.RemoteAddress;
import com.waterfeeds.gproxy.zookeeper.ZookeeperService;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;

public class ProxyApplication {
    public static void main(String[] args) {
        registerProxy();
        startProxy();
    }

    public static void startProxy() {
        DefaultServerApiService proxyService = new DefaultServerApiService();
        proxyService.setPort(8080);
        Proxy proxy = new Proxy();
        ProxyChannelInitializer proxyInitializer = new ProxyChannelInitializer(proxy);
        proxyService.setChannelInitializer(proxyInitializer);
        DefaultClientApiService clientApiService = DefaultClientApiService.newInstance(4);
        ForwardChannelInitializer forwardInitializer = new ForwardChannelInitializer(proxy);
        clientApiService.setChannelInitializer(forwardInitializer);
        proxy.setClientApiService(clientApiService);
        boolean status = proxyService.start();
        if (!status)
            return;
        ZookeeperService zookeeperService = new ZookeeperService();
        zookeeperService.setPath("gproxy");
        zookeeperService.setZkAddress("127.0.0.1:2181");
        zookeeperService.setCertificate(new Certificate());
        try {
            zookeeperService.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String nameSpace = Const.ZOOKEEPER_NAMESPACE_SERVERS;
        if (zookeeperService.exists(nameSpace)) {
            RemoteAddress[] addresses = zookeeperService.getChildNodes(nameSpace);
            for (RemoteAddress address: addresses) {
                proxy.addServerAddress(address.getNodeName(), address.getUri());
            }
        }
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
