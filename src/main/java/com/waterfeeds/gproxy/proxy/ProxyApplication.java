package com.waterfeeds.gproxy.proxy;

import com.waterfeeds.gproxy.message.Const;
import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.network.DefaultClientApiService;
import com.waterfeeds.gproxy.network.DefaultServerApiService;
import com.waterfeeds.gproxy.proxy.handler.*;
import com.waterfeeds.gproxy.user.Properties;
import com.waterfeeds.gproxy.zookeeper.Certificate;
import com.waterfeeds.gproxy.zookeeper.RemoteAddress;
import com.waterfeeds.gproxy.zookeeper.ZookeeperService;
import com.waterfeeds.gproxy.zookeeper.base.NodeEventHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;

public class ProxyApplication {
    public static void main(String[] args) {
        startProxy(Properties.PROXY_ONE_PORT, Properties.getZkAddress(), Properties.SPACE);
    }

    public static void startProxy(int port, String zkAddress, String space) {
        Proxy proxy = new Proxy();
        DefaultServerApiService proxyService = new DefaultServerApiService();
        proxyService.setPort(port);
        ProxyChannelInitializer proxyInitializer = new ProxyChannelInitializer(proxy);
        proxyService.setChannelInitializer(proxyInitializer);
        DefaultClientApiService clientApiService = DefaultClientApiService.newInstance(2);
        ForwardChannelInitializer forwardInitializer = new ForwardChannelInitializer(proxy);
        clientApiService.setChannelInitializer(forwardInitializer);
        proxy.setClientApiService(clientApiService);
        boolean status = proxyService.start();
        if (!status)
            return;
        ZookeeperService zookeeperService = new ZookeeperService();
        zookeeperService.setPath(space);
        zookeeperService.setZkAddress(zkAddress);
        zookeeperService.setCertificate(new Certificate());
        NodeEventHandler eventHandler = new ProxyNodeEventHandler(proxy);
        try {
            zookeeperService.afterPropertiesSet(eventHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String nameSpace = Const.ZOOKEEPER_NAMESPACE_SERVERS;
        /*if (zookeeperService.exists(nameSpace)) {
            RemoteAddress[] addresses = zookeeperService.getChildNodes(nameSpace);
            for (RemoteAddress address: addresses) {
                proxy.addServerAddress(address.getNodeName(), address.getUri());
            }
        }*/
        zookeeperService.setPathChildrenListener(nameSpace);
    }

}
