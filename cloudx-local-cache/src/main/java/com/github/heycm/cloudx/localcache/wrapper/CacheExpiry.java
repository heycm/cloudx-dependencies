package com.github.heycm.cloudx.localcache.wrapper;

import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;

/**
 * 缓存过期策略
 * @author heycm
 * @version 1.0
 * @since 2025/8/23 14:32
 */
public class CacheExpiry implements Expiry<Object, CacheValue<Object>> {

    /**
     * 写操作（创建）后更新存活时间，单位纳秒，若设置过期时间<1则视为永不过期
     * @param key         键
     * @param value       值
     * @param currentTime 当前时间（纳秒）
     * @return 该键存活时长（纳秒）
     */
    @Override
    public long expireAfterCreate(Object key, CacheValue<Object> value, long currentTime) {
        return value.getTtlNanos() > 0 ? value.getTtlNanos() : Long.MAX_VALUE;
    }

    /**
     * 写操作（更新）后更新存活时间，单位纳秒，若设置过期时间<1则视为永不过期
     * @param key             键
     * @param value           值
     * @param currentTime     当前时间（纳秒）
     * @param currentDuration 当前键存活时长（纳秒）
     * @return 该键存活时长（纳秒）
     */
    @Override
    public long expireAfterUpdate(Object key, CacheValue<Object> value, long currentTime, @NonNegative long currentDuration) {
        return value.getTtlNanos() > 0 ? value.getTtlNanos() : Long.MAX_VALUE;
    }

    /**
     * 读操作后更新存活时间，返回 currentDuration 表示读操作不改变过期时间
     * @param key             键
     * @param value           值
     * @param currentTime     当前时间（纳秒）
     * @param currentDuration 当前键存活时长（纳秒）
     * @return 该键存活时长（纳秒）
     */
    @Override
    public long expireAfterRead(Object key, CacheValue<Object> value, long currentTime, @NonNegative long currentDuration) {
        return currentDuration;
    }
}
