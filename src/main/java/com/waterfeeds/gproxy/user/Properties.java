package com.waterfeeds.gproxy.user;

import com.waterfeeds.gproxy.cluster.LoadBalance;
import com.waterfeeds.gproxy.cluster.LoadBalanceSelector;
import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.zookeeper.RemoteAddress;

import java.util.Arrays;
import java.util.List;

public class Properties {
    public final static int PROXY_ONE_PORT = 8080;
    public final static String PROXY_ONE_ADDRESS = "127.0.0.1:8080";

    public final static int PROXY_TWO_PORT = 8079;
    public final static String PROXY_TWO_ADDRESS = "127.0.0.7:8079";

    public final static String SPACE = "gproxy";

    public final static int SERVER_ONE_PORT = 8081;
    public final static String SERVER_ONE_NAME = "/server-01";
    public final static String SERVER_ONE_ADDRESS = "127.0.0.1:8081";

    public final static int SERVER_TWO_PORT = 8082;
    public final static String SERVER_TWO_NAME = "/server-02";
    public final static String SERVER_TWO_ADDRESS = "127.0.0.1:8082";

    public final static int SERVER_THREE_PORT = 8083;
    public final static String SERVER_THREE_NAME = "/server-03";
    public final static String SERVER_THREE_ADDRESS = "127.0.0.1:8083";

    public final static List<String> zkAddresses = Arrays.asList("127.0.0.1:2181", "127.0.0.1:2182", "127.0.0.1:2183");

    public static RemoteAddress[] parseAddress() {
        int length = zkAddresses.size();
        RemoteAddress[] addresses = new RemoteAddress[length];
        for (int i = 0; i < length; i++) {
            String zkAddress = zkAddresses.get(i);
            URI uri = new URI(zkAddress);
            addresses[i] = new RemoteAddress(uri);
        }
        return addresses;
    }

    public static String getZkAddress() {
        if (zkAddresses.size() == 1) {
            return zkAddresses.get(0);
        } else {
            String mode = "random";
            LoadBalanceSelector selector = new LoadBalanceSelector();
            LoadBalance loadBalance = selector.selectLoadBalance(mode);
            String zkAddress = loadBalance.selectRandom(parseAddress()).getAddress();
            System.out.println("zkAddress: " + zkAddress);
            return zkAddress;
        }
    }
}
