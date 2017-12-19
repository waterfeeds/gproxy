package com.waterfeeds.gproxy.zookeeper.base;

import com.waterfeeds.gproxy.zookeeper.Certificate;
import org.apache.curator.framework.CuratorFramework;

public interface BaseZookeeperClient {
    CuratorFramework init(String path, String connectString, Certificate certificate);
}
