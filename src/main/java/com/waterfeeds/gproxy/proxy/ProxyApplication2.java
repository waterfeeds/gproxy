package com.waterfeeds.gproxy.proxy;

import com.waterfeeds.gproxy.message.Const;
import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.DefaultClientApiService;
import com.waterfeeds.gproxy.network.DefaultServerApiService;
import com.waterfeeds.gproxy.proxy.handler.ForwardChannelInitializer;
import com.waterfeeds.gproxy.proxy.handler.ProxyChannelInitializer;
import com.waterfeeds.gproxy.proxy.handler.ProxyNodeEventHandler;
import com.waterfeeds.gproxy.proxy.handler.ProxyNodeEventHandler2;
import com.waterfeeds.gproxy.user.Properties;
import com.waterfeeds.gproxy.zookeeper.Certificate;
import com.waterfeeds.gproxy.zookeeper.RemoteAddress;
import com.waterfeeds.gproxy.zookeeper.ZookeeperService;
import com.waterfeeds.gproxy.zookeeper.base.NodeEventHandler;
import org.apache.zookeeper.CreateMode;

public class ProxyApplication2 {
    public static void main(String[] args) {
        startProxy(Properties.PROXY_TWO_PORT, Properties.getZkAddress(), Properties.SPACE);
    }

    public static void startProxy(int port, String zkAddress, String space) {
        DefaultServerApiService proxyService = new DefaultServerApiService();
        proxyService.setPort(port);
        Proxy proxy = new Proxy();
        ProxyChannelInitializer proxyInitializer = new ProxyChannelInitializer(proxy);
        proxyService.setChannelInitializer(proxyInitializer);
        DefaultClientApiService clientApiService = DefaultClientApiService.newInstance(2);
        ForwardChannelInitializer forwardInitializer = new ForwardChannelInitializer(proxy);
        clientApiService.setChannelInitializer(forwardInitializer);
        proxy.setClientApiService(clientApiService);
        boolean status = proxyService.start();
        if (!status) {
            return;
        }
        ZookeeperService zookeeperService = new ZookeeperService();
        zookeeperService.setPath(space);
        zookeeperService.setZkAddress(zkAddress);
        zookeeperService.setCertificate(new Certificate());
        NodeEventHandler eventHandler = new ProxyNodeEventHandler2(proxy);
        try {
            zookeeperService.afterPropertiesSet(eventHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String nameSpace = Const.ZOOKEEPER_NAMESPACE_SERVERS;
        zookeeperService.setPathChildrenListener(nameSpace);
    }
}
