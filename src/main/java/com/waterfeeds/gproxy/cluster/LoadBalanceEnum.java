package com.waterfeeds.gproxy.cluster;

public enum LoadBalanceEnum {
    RANDOM("random"),
    ROBIN("robin"),
    IP("ip");
    private LoadBalance loadBalance;

    LoadBalanceEnum(String loadBalanceName) {
        switch (loadBalanceName) {
            case "random":
                this.loadBalance = new RandomLoadBalance();
                break;
            case "robin":
                this.loadBalance = new RoundRobinLoadBalance();
                break;
            case "ip":
                this.loadBalance = new IpRandomLoadBalance();
                break;
        }
    }

    public LoadBalance getLoadBalance() {
        return loadBalance;
    }
}
