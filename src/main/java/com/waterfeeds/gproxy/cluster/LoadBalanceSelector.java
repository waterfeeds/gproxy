package com.waterfeeds.gproxy.cluster;

public class LoadBalanceSelector {
    public LoadBalance selectLoadBalance(String loadBalanceName) {
        switch (loadBalanceName) {
            case "random":
                return LoadBalanceEnum.RANDOM.getLoadBalance();
            case "robin":
                return LoadBalanceEnum.ROBIN.getLoadBalance();
            case "ip":
                return LoadBalanceEnum.IP.getLoadBalance();
            default:
                return LoadBalanceEnum.RANDOM.getLoadBalance();
        }
    }
}
