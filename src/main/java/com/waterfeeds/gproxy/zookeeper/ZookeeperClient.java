package com.waterfeeds.gproxy.zookeeper;

import com.waterfeeds.gproxy.message.Const;
import com.waterfeeds.gproxy.zookeeper.base.BaseZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.List;

public class ZookeeperClient implements BaseZookeeperClient {
    @Override
    public CuratorFramework init(String path, final String connectString, final Certificate certificate) {
        ACLProvider aclProvider = new ACLProvider() {
            private List<ACL> acl;
            @Override
            public List<ACL> getDefaultAcl() {
                if (acl == null) {
                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
                    acl.clear();
                    acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", certificate.toString())));
                    this.acl = acl;
                }
                return acl;
            }

            @Override
            public List<ACL> getAclForPath(String s) {
                return acl;
            }
        };
        String scheme = certificate.getScheme();
        byte[] auth = certificate.toString().getBytes();
        int connectionTimeoutMs = Const.ZOOKEEPER_CONNECTION_TIME_OUT;
        String namespace = path;
        CuratorFramework client = CuratorFrameworkFactory.builder().aclProvider(aclProvider)
                .authorization(scheme, auth)
                .connectionTimeoutMs(connectionTimeoutMs)
                .connectString(connectString)
                .namespace(namespace)
                .sessionTimeoutMs(3000)
                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).build();
        client.start();
        return client;
    }
}
