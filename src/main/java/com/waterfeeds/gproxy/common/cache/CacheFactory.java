package com.waterfeeds.gproxy.common.cache;

import java.util.concurrent.ConcurrentHashMap;

public class CacheFactory<K, V> implements Cache<K, V> {
    private ConcurrentHashMap<K, V> commons = new ConcurrentHashMap<K, V>();
    @Override
    public V getCache(K k) {
        return commons.get(k);
    }

    @Override
    public void addCache(K k, V v) {
        commons.put(k, v);
    }

    @Override
    public void putIfAbsentCache(K k, V v) {
        commons.putIfAbsent(k, v);
    }

    @Override
    public void remove(K k) {
        commons.remove(k);
    }
}
