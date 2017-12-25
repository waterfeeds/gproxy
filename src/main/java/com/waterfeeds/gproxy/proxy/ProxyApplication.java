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

public class ProxyApplication {
    public static void main(String[] args) {
        startProxy();
    }

    public static void startProxy() {
        DefaultServerApiService serverProxy = new DefaultServerApiService();
        serverProxy.setPort(8080);
        Proxy proxy = new Proxy();
        ProxyHandler handler = new ProxyHandler(proxy);
        ProxyChannelInitializer proxyInitializer = new ProxyChannelInitializer();
        proxyInitializer.init(handler);
        serverProxy.setChannelInitializer(proxyInitializer);
        DefaultClientApiService clientApiService = DefaultClientApiService.newInstance(4);
        ForwardHandler forwardHandler = new ForwardHandler(proxy);
        ForwardChannelInitializer forwardInitializer = new ForwardChannelInitializer();
        forwardInitializer.init(forwardHandler);
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
            System.out.println("server address:" + address);
        }
        if (!StringUtils.isBlank(address) && uri.parseAddress(address)) {
            proxy.addServerAddress(serverId, uri);
        }
        serverProxy.start();
    }
}
