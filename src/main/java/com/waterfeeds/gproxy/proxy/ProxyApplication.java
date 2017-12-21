package com.waterfeeds.gproxy.proxy;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.DefaultServerApiService;
import com.waterfeeds.gproxy.proxy.handler.ProxyChannelInitializer;
import com.waterfeeds.gproxy.proxy.handler.ProxyHandler;
import com.waterfeeds.gproxy.zookeeper.Certificate;
import com.waterfeeds.gproxy.zookeeper.ZookeeperService;
import org.apache.commons.lang3.StringUtils;

public class ProxyApplication {
    public static void main(String[] args) {
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
        DefaultServerApiService serverProxy = new DefaultServerApiService();
        serverProxy.setPort(8080);
        Proxy proxy = new Proxy();
        URI uri = new URI();
        if (!StringUtils.isBlank(address) && uri.parseAddress(address)) {
            proxy.addServerAddress(serverId, uri);
        }
        ProxyHandler handler = new ProxyHandler(proxy);
        ProxyChannelInitializer initializer = new ProxyChannelInitializer();
        initializer.init(handler);
        serverProxy.setChannelInitializer(initializer);
        serverProxy.start();
    }
}
