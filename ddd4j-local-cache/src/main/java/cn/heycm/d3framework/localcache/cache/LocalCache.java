package cn.heycm.d3framework.localcache.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存接口
 * @author heycm
 * @version 1.0
 * @since 2025/8/21 21:23
 */
public interface LocalCache {

    /* ---- 基础 KV ---- */

    <K, V> V get(K key);

    <K, V> V get(K key, Callable<V> loader);

    <K, V> void set(K key, V value);

    <K, V> void set(K key, V value, long ttl, TimeUnit timeUnit);

    <K> void invalidate(K... keys);

    void invalidateAll();
}
