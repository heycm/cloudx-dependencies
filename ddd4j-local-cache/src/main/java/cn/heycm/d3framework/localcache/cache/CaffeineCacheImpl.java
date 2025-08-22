package cn.heycm.d3framework.localcache.cache;

import cn.heycm.d3framework.localcache.wrapper.CacheValue;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.NonNegative;
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
        this.cache = Caffeine.newBuilder()
                // 最大缓存数量
                .maximumSize(maximumSize)
                // 缓存过期策略
                .expireAfter(new Expiry<Object, CacheValue<Object>>() {
                    @Override
                    public long expireAfterCreate(Object key, CacheValue<Object> value, long currentTime) {
                        // （创建）写操作后更新存活时间，单位纳秒，若设置过期时间<1则视为永不过期
                        return value.getTtlNanos() > 0 ? value.getTtlNanos() : Long.MAX_VALUE;
                    }

                    @Override
                    public long expireAfterUpdate(Object key, CacheValue<Object> value, long currentTime,
                            @NonNegative long currentDuration) {
                        // （更新）写操作后更新存活时间，单位纳秒，若设置过期时间<1则视为永不过期
                        return value.getTtlNanos() > 0 ? value.getTtlNanos() : Long.MAX_VALUE;
                    }

                    @Override
                    public long expireAfterRead(Object key, CacheValue<Object> value, long currentTime, @NonNegative long currentDuration) {
                        // 读操作不改变过期时间
                        return currentDuration;
                    }
                })
                // 软引用，当内存不足时（OOM）, 会自动回收
                .softValues().build();
        this.cleaner = Thread.ofVirtual().name("CFC-Cleaner-" + cacheId).start(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    // 主动触发 cache 过期机制
                    cache.cleanUp();
                    Thread.sleep(cleanInterval);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
               log.info("CFC-Cleaner-{} thread stopped gracefully.", cacheId);
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

    /**
     * 主动关闭清理线程
     */
    public void close() {
        cleaner.interrupt();
    }

    @Override
    public void destroy() throws Exception {
        log.info("LocalCache destroy...");
        close();
    }
}
