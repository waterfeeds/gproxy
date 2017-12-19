package com.waterfeeds.gproxy.zookeeper;

import com.waterfeeds.gproxy.message.URI;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.security.cert.Certificate;
import java.util.List;

public class ZookeeperService implements BaseZookeeperService, InitializingBean, DisposableBean {
    protected static final Logger log = LoggerFactory.getLogger(ZookeeperService.class.getSimpleName());
    private String DEV_S = "/";
    private String NODE_NAME = "node_";
    private int port;
    private String zkAddress;
    private Certificate certificate;
    private String path;
    //private NodeEventHandler eventHandler;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private CuratorFramework curatorFramework;

    public void registerNode(String path, URI uri, CreateMode mode, boolean is) {
        path = buildPath(path);
        try {
            byte[] bytes = new byte[100];
            if (is)
                curatorFramework.create().creatingParentsIfNeeded().withMode(mode).forPath(path, bytes);
            else
                curatorFramework.create().withMode(mode).forPath(path, bytes);
        } catch (Exception e) {
            log.error(this.getClass().getName() + "创建节点失败");
        }
    }

    public void removeNode(String path, boolean is) {
        path = path.startsWith("/") ? path : "/" + path;
        try {
            if (is) {
                curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
            } else {
                curatorFramework.delete().forPath(path);
            }
        } catch (Exception e) {
            log.error(this.getClass().getName() + "删除节点失败");
        }
    }

    public boolean exists(String path) {
        path = path.startsWith("/") ? path : "/" + path;
        Stat forPath;
        try {
            forPath = curatorFramework.checkExists().forPath(path);
            if (forPath == null)
                return false;
            else
                return true;
        } catch (Exception e) {
            log.error(this.getClass().getName() + "检查节点是否存在失败", e);
        }
        return false;
    }

    public RemoteAddress[] getChildNodes(String path) {
        path = path.startsWith(DEV_S) ? path : DEV_S + path;
        try {
            List<String> forPath = curatorFramework.getChildren().forPath(path);
            RemoteAddress[] addresses = new RemoteAddress[forPath.size()];
            StringBuilder sb = new StringBuilder();
            int num = 0;
            for (String paths: forPath) {
                String final_path = sb.append(path).append(DEV_S).append(paths).toString();
                URI data = this.getData(final_path);
                if (data != null) {
                    RemoteAddress address = new RemoteAddress(paths, data);
                    addresses[num] = address;
                    num ++;
                }
                sb.delete(0, sb.length());
            }
            return addresses;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public URI getData(String path) {
        if (exists(path)) {
            byte[] forPath;
            try {
                forPath = curatorFramework.getData().forPath(path);
                if (forPath != null) {
                    return new URI();
                }
                return null;
            } catch (Exception e) {
                log.error(this.getClass().getName() + "获取节点数据失败", e);
            }
        }
        return null;
    }

    private String buildPath(String path) {
        path = path.startsWith(DEV_S) ? path : DEV_S + path;
        StringBuilder builder = new StringBuilder(path);
        builder.append(DEV_S).append(NODE_NAME);
        return builder.toString();
    }

    public void closeServer() {
        curatorFramework.close();
    }

    public void destroy() throws Exception {

    }

    public void afterPropertiesSet() throws Exception {

    }
}
