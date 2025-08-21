package cn.heycm.d3framework.localcache.cache;

import cn.heycm.d3framework.localcache.wrapper.CacheValue;
import com.github.benmanes.caffeine.cache.Cache;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine 缓存实现
 * @author heycm
 * @version 1.0
 * @since 2025/8/21 22:11
 */
@SuppressWarnings("unchecked")
public class CaffeineTTLCacheImpl implements LocalCache {

    private final Cache<Object, Object> cache;

    public CaffeineTTLCacheImpl(Cache<Object, Object> cache) {
        this.cache = cache;
    }

    @Override
    public <K, V> V get(K key) {
        CacheValue<V> wrapper = (CacheValue<V>) cache.getIfPresent(key);
        if (wrapper == null || wrapper.isExpired()) {
            cache.invalidate(wrapper);
            return null;
        }
        return wrapper.getValue();
    }

    @Override
    public <K, V> V get(K key, Callable<V> loader) {
        V v = this.get(key);
        if (v != null) return v;
        try {
            V call = loader.call();
            this.set(key, call);
            return call;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <K, V> void set(K key, V value) {
        cache.put(key, CacheValue.of(value, -1));
    }

    @Override
    public <K, V> void set(K key, V value, long ttl, TimeUnit timeUnit) {
        cache.put(key, CacheValue.of(value, timeUnit.toMillis(ttl)));
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
    public <K, F, V> V hGet(K key, F field) {
        Map<F, V> map = get(key);
        return map == null ? null : map.get(field);
    }

    @Override
    public <K, F, V> void hPut(K key, F field, V value) {
        cache.asMap().compute(key, (k, v) -> {
            CacheValue<Map<F, V>> wrapper = (CacheValue<Map<F, V>>) v;
            if (wrapper == null) {
                wrapper = CacheValue.of(new ConcurrentHashMap<>(), -1);
            }
            wrapper.getValue().put(field, value);
            return wrapper;
        });
    }

    @Override
    public <K, F, V> V hRemove(K key, F field) {
        Map<F, V> map = get(key);
        return map == null ? null : map.remove(field);
    }

    @Override
    public <K, F, V> Map<F, V> hGetAll(K key) {
        return get(key);
    }

    @Override
    public <K, F> boolean hExists(K key, F field) {
        return false;
    }

    @Override
    public <K, V> void lPush(K key, V value) {

    }

    @Override
    public <K, V> void rPush(K key, V value) {

    }

    @Override
    public <K, V> V lPop(K key) {
        return null;
    }

    @Override
    public <K, V> V rPop(K key) {
        return null;
    }

    @Override
    public <K, V> List<V> lRange(K key, int start, int end) {
        return List.of();
    }

    @Override
    public <K> int lLength(K key) {
        return 0;
    }

    @Override
    public <K> int lIndex(K key, int index) {
        return 0;
    }

    @Override
    public <K, V> void lSet(K key, int index, V value) {

    }

    @Override
    public <K, V> V lGet(K key, int index) {
        return null;
    }

    @Override
    public <K, V> List<V> lGetAll(K key) {
        return List.of();
    }

    @Override
    public <K, V> void sAdd(K key, V value) {

    }

    @Override
    public <K, V> void sRemove(K key, V value) {

    }

    @Override
    public <K, V> boolean sIsMember(K key, V value) {
        return false;
    }

    @Override
    public <K, V> Set<V> sMembers(K key) {
        return Set.of();
    }
}
