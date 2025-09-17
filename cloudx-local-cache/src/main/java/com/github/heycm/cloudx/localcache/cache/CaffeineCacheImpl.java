package com.github.heycm.cloudx.localcache.cache;

import com.github.heycm.cloudx.localcache.wrapper.CacheExpiry;
import com.github.heycm.cloudx.localcache.wrapper.CacheValue;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

/**
 * Caffeine 缓存实现
 * @author heycm
 * @version 1.0
 * @since 2025/8/21 22:11
 */
@Slf4j
@SuppressWarnings("unchecked")
public class CaffeineCacheImpl implements LocalCache, DisposableBean {

    private static final AtomicInteger CACHE_ID = new AtomicInteger(0);

    private final int cacheId;

    private final Cache<Object, CacheValue<Object>> cache;

    private final long cleanInterval = 1000;

    private final Thread cleaner;

    public CaffeineCacheImpl(long maximumSize) {
        this.cacheId = CACHE_ID.incrementAndGet();
        this.cache = Caffeine.newBuilder().maximumSize(maximumSize) // 最大缓存数量
                .expireAfter(new CacheExpiry()) // 缓存过期策略
                .softValues() // 软引用，OOM时被回收
                .build();
        this.cleaner = Thread.ofVirtual().name("LC-Cleaner-" + cacheId).start(() -> {
            log.info("LC-Cleaner-{} thread started.", cacheId);
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    // 主动触发 cache 过期机制
                    cache.cleanUp();
                    Thread.sleep(cleanInterval);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                log.info("LC-Cleaner-{} thread stopped gracefully.", cacheId);
            }
        });
    }

    @Override
    public <K, V> V get(K key) {
        CacheValue<Object> cacheValue = cache.getIfPresent(key);
        return cacheValue == null ? null : (V) cacheValue.getValue();
    }

    @Override
    public <K, V> V get(K key, Callable<V> loader) {
        V value = get(key);
        if (value != null) {
            return value;
        }
        try {
            value = loader.call();
            set(key, value);
            return value;
        } catch (Exception e) {
            throw new RuntimeException("Load value failed for key: " + key, e);
        }
    }

    @Override
    public <K, V> void set(K key, V value) {
        if (value != null) {
            cache.put(key, new CacheValue<>(value, -1));
        }
    }

    @Override
    public <K, V> void set(K key, V value, long ttl, TimeUnit timeUnit) {
        if (value != null) {
            cache.put(key, new CacheValue<>(value, timeUnit.toNanos(ttl)));
        }
    }

    @Override
    public <K> void invalidate(K... keys) {
        if (keys != null) {
            for (K key : keys) {
                cache.invalidate(key);
            }
        }
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }

    @Override
    public void destroy() throws Exception {
        log.info("LocalCache-{} destroy...", cacheId);
        close();
    }

    /**
     * 主动关闭清理线程
     */
    public void close() {
        cleaner.interrupt();
    }
}
