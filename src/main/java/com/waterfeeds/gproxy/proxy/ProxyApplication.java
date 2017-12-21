package com.waterfeeds.gproxy.proxy;

import com.waterfeeds.gproxy.network.DefaultServerApiService;

public class ProxyApplication {
    public static void main(String[] args) {
        /*ZookeeperService zookeeperService = new ZookeeperService();
        zookeeperService.setPath("gproxy");
        zookeeperService.setZkAddress("127.0.0.1:2181");
        zookeeperService.setCertificate(new Certificate());
        try {
            zookeeperService.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (zookeeperService.exists("/server-01")) {
            String address = zookeeperService.getData("/server-01");
            System.out.println("server address:" + address);
        }*/
        DefaultServerApiService serverProxy = new DefaultServerApiService();
        serverProxy.setPort(8080);
        serverProxy.start();
    }
}
