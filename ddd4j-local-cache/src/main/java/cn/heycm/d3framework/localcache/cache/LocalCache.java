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

    /**
     * 获取缓存
     * @param key 缓存键
     * @param <K> 缓存键类型
     * @param <V> 缓存值类型
     * @return 缓存值
     */
    <K, V> V get(K key);

    /**
     * 获取缓存，缓存键不存在时加载缓存值，并写入缓存
     * @param key    缓存键
     * @param loader 加载方法
     * @param <K>    缓存键类型
     * @param <V>    缓存值类型
     * @return 缓存值
     */
    <K, V> V get(K key, Callable<V> loader);

    /**
     * 设置缓存
     * @param key   缓存键
     * @param value 缓存值
     * @param <K>   缓存键类型
     * @param <V>   缓存值类型
     */
    <K, V> void set(K key, V value);

    /**
     * 设置缓存，并设置缓存有效期
     * @param key      缓存键
     * @param value    缓存值
     * @param ttl      缓存存活时间
     * @param timeUnit 缓存存活时间单位
     * @param <K>      缓存键类型
     * @param <V>      缓存值类型
     */
    <K, V> void set(K key, V value, long ttl, TimeUnit timeUnit);

    /**
     * 删除缓存
     * @param keys 缓存键
     * @param <K>  缓存键类型
     */
    <K> void invalidate(K... keys);

    /**
     * 删除所有缓存
     */
    void invalidateAll();
}
