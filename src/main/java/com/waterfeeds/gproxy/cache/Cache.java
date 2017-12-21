package com.waterfeeds.gproxy.cache;

public interface Cache<K, V> {
    V getCache(K k);

    void addCache(K k, V v);

    void putIfAbsentCache(K k, V v);

    void remove(K k);
}
