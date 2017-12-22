package com.waterfeeds.gproxy.cluster;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.zookeeper.RemoteAddress;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class IpRandomLoadBalance implements LoadBalance {
    String name = "ip";

    @Override
    public URI selectRandom(RemoteAddress[] uris) {
        if (uris != null && uris.length > 0) {
            try {
                String hostAddress = Inet4Address.getLocalHost().getHostAddress();
                Long hash = MurMurHash.hash(hostAddress);
                hash = Math.abs(hash);
                int ipIndex = (int) (hash == null ? 0 : (hash % uris.length));
                return uris[ipIndex].getUri();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
