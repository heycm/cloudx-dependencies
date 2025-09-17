package com.github.heycm.cloudx.localcache.wrapper;

import lombok.Getter;
import lombok.NonNull;

/**
 * TTL缓存值
 * @author heycm
 * @version 1.0
 * @since 2025/8/21 22:03
 */
@Getter
public class CacheValue<V> {

    /**
     * 缓存值
     */
    private final V value;

    /**
     * 缓存有效期，单位纳秒
     */
    private final long ttlNanos;

    public CacheValue(@NonNull V value, long ttlNanos) {
        this.value = value;
        this.ttlNanos = ttlNanos > 0 ? ttlNanos : -1;
    }

    @Override
    public String toString() {
        return "CacheValue[value: " + value + ", ttlNanos: " + ttlNanos + "]";
    }
}
