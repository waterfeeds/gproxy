package com.waterfeeds.gproxy.cluster;

import com.waterfeeds.gproxy.message.URI;
import com.waterfeeds.gproxy.zookeeper.RemoteAddress;

import java.util.Random;

public class RandomLoadBalance implements LoadBalance {
    String name = "random";

    @Override
    public URI selectRandom(RemoteAddress[] uris) {
        if (uris != null && uris.length > 0) {
            int length = uris.length;
            int randIndex = new Random().nextInt(length);
            return uris[randIndex].getUri();
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
