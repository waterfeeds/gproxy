package com.waterfeeds.gproxy.zookeeper;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.zookeeper.base.BaseZookeeperClient;
import com.waterfeeds.gproxy.zookeeper.base.BaseZookeeperService;
import io.netty.util.internal.ConcurrentSet;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZookeeperService implements BaseZookeeperService, InitializingBean, DisposableBean {
    protected static final Logger log = LoggerFactory.getLogger(ZookeeperService.class.getSimpleName());
    private String DEV_S = "/";
    private String NODE_NAME = "node_";
    private int port;
    private String zkAddress;
    private Certificate certificate;
    private String path;
    private NodeEventHandler eventHandler;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private BaseZookeeperClient baseZookeeperClient;
    private CuratorFramework curatorFramework;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void registerNode(String path, URI uri, CreateMode mode, byte[] bytes, boolean is) {
        //path = buildPath(path);
        try {
            if (is) {
                curatorFramework.create().creatingParentsIfNeeded().withMode(mode).forPath(path, bytes);
            } else {
                curatorFramework.create().withMode(mode).forPath(path, bytes);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(this.getClass().getName() + " 创建节点失败");
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
            System.out.println(this.getClass().getName() + "删除节点失败");
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
            for (String paths : forPath) {
                String final_path = sb.append(path).append(DEV_S).append(paths).toString();
                String data = this.getData(final_path);
                if (data != null) {
                    //RemoteAddress address = new RemoteAddress(paths, data);
                    //addresses[num] = address;
                    num++;
                }
                sb.delete(0, sb.length());
            }
            return addresses;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getData(String path) {
        if (exists(path)) {
            byte[] forPath;
            try {
                forPath = curatorFramework.getData().forPath(path);
                if (forPath != null) {
                    return new String(forPath);
                }
                return null;
            } catch (Exception e) {
                log.error(this.getClass().getName() + "获取节点数据失败", e);
            }
        }
        return null;
    }

    public void setPathChildrenListener(String path) {
        path = path.startsWith(DEV_S) ? path : DEV_S + path;
        @SuppressWarnings("resource")
        PathChildrenCache childrenCache = new PathChildrenCache(this.curatorFramework, path, true);
        PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData data = event.getData();
                if (data != null) {
                    URI uri = null;
                    String path = data.getPath();
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            eventHandler.addNode(path, uri);
                            logger.info("NODE_ADDED : (" + path + ")  数据:" + uri.toString());
                            break;
                        case CHILD_REMOVED:
                            eventHandler.removeNode(path);
                            logger.info("NODE_REMOVED : " + path);
                            break;
                        case CHILD_UPDATED:
                            eventHandler.updateNode(path, uri);
                            logger.info("NODE_UPDATED : (" + path + ")  数据:" + uri.toString());
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        childrenCache.getListenable().addListener(childrenCacheListener);
        System.out.println("Register zk watcher successfully!");
        try {
            childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("", e);
        }
    }

    public void setTreeListener(String path) throws Exception {
        path = path.startsWith(DEV_S) ? path : DEV_S + path;
        //设置节点的cache
        @SuppressWarnings("resource")
        TreeCache treeCache = new TreeCache(this.curatorFramework, path);
        //设置监听器和处理过程
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData data = event.getData();
                if (data != null) {
                    URI uri = null;

                    String path = data.getPath();
                    switch (event.getType()) {
                        case NODE_ADDED:
                            eventHandler.addNode(path, uri);
                            logger.info("NODE_ADDED : (" + path + ")  数据:" + uri.toString());
                            break;
                        case NODE_REMOVED:
                            eventHandler.removeNode(path);
                            logger.info("NODE_REMOVED : " + path);
                            break;
                        case NODE_UPDATED:
                            eventHandler.updateNode(path, uri);
                            logger.info("NODE_UPDATED : (" + path + ")  数据:" + uri.toString());
                            break;
                        default:
                            break;
                    }
                } else {
                    logger.info("data is null : " + event.getType());
                }
            }
        });
        //开始监听
        treeCache.start();
    }

    public interface NodeEventHandler {
        void addNode(String serviceName, URI uri);

        void removeNode(String address);

        void updateNode(String serviceName, URI uri);
    }

    public void initRegister(ConcurrentSet<String> registerServiceNames) throws Exception {
        if (CollectionUtils.isNotEmpty(registerServiceNames)) {
            InetAddress localHost = Inet4Address.getLocalHost();
            String hostAddress = localHost.getHostAddress();
            for (String serviceName : registerServiceNames) {
                URI uri = new URI(null, hostAddress, this.port, null);
                registerNode(serviceName, uri, CreateMode.EPHEMERAL_SEQUENTIAL, new byte[100], true);
                System.out.println("success register server {}: {}");
            }
        }
    }

    public void initServer(ConcurrentSet<String> serviceNames) throws Exception {
        if (CollectionUtils.isNotEmpty(serviceNames)) {
            for (String serviceName : serviceNames) {
                RemoteAddress[] addresses = getChildNodes(serviceName);
                if (addresses != null) {
                    setPathChildrenListener(serviceName);
                    setTreeListener(serviceName);
                } else {
                    System.out.println("not register server");
                }
            }
        }
    }

    public void closeServer() {
        curatorFramework.close();
    }

    public void destroy() throws Exception {
        this.closeServer();
    }

    @Override
    public void afterPropertiesSet() {
        this.baseZookeeperClient = new ZookeeperClient();
        this.curatorFramework = baseZookeeperClient.init(path, zkAddress, certificate);
        this.eventHandler = new NodeEventHandler() {
            @Override
            public void addNode(String serviceName, URI uri) {
                String matchPath = matchPath(path);
                String finalPath = path.substring(path.lastIndexOf('/') + 1, path.length());
                if (uri != null) {

                }
            }

            @Override
            public void removeNode(String address) {

            }

            @Override
            public void updateNode(String serviceName, URI uri) {

            }
        };
    }

    private String buildPath(String path) {
        path = path.startsWith(DEV_S) ? path : DEV_S + path;
        StringBuilder builder = new StringBuilder(path);
        builder.append(DEV_S).append(NODE_NAME);
        return builder.toString();
    }

    private String matchPath(String path) {
        String regex = "/(.*?);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public boolean isExists(RemoteAddress[] addresses, String path) {
        boolean flag = false;
        for (RemoteAddress address : addresses) {
            if (path.contains(address.getNodeName())) {
                flag = true;
                break;
            }
        }
        return flag;
    }


}
