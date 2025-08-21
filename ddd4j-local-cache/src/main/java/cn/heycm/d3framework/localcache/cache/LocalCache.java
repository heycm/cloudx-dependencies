package cn.heycm.d3framework.localcache.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;
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

    /* ---- Map 结构 ---- */

    <K, F, V> V hGet(K key, F field);

    <K, F, V> void hPut(K key, F field, V value);

    <K, F, V> V hRemove(K key, F field);

    <K, F, V> Map<F, V> hGetAll(K key);

    <K, F> boolean hExists(K key, F field);

    /* ---- List 结构 ---- */

    <K, V> void lPush(K key, V value);

    <K, V> void rPush(K key, V value);

    <K, V> V lPop(K key);

    <K, V> V rPop(K key);

    <K, V> List<V> lRange(K key, int start, int end);

    <K> int lLength(K key);

    <K> int lIndex(K key, int index);

    <K, V> void lSet(K key, int index, V value);

    <K, V> V lGet(K key, int index);

    <K, V> List<V> lGetAll(K key);

    /* ---- Set 结构 ---- */

    <K, V> void sAdd(K key, V value);

    <K, V> void sRemove(K key, V value);

    <K, V> boolean sIsMember(K key, V value);

    <K, V> Set<V> sMembers(K key);
}
