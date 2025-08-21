package cn.heycm.d3framework.localcache.wrapper;

import lombok.Getter;

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
     * 过期时间
     */
    private final long expireTime;

    public CacheValue(V value, long expireTime) {
        this.value = value;
        this.expireTime = expireTime <= 0 ? -1 : System.currentTimeMillis() + expireTime;
    }

    public static <V> CacheValue<V> of(V value, long expireTime) {
        return new CacheValue<>(value, expireTime);
    }

    public boolean isExpired() {
        return expireTime > 0 && System.currentTimeMillis() > expireTime;
    }

    @Override
    public final int hashCode() {
        return value.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof CacheValue<?> other) {
            return value.equals(other.value);
        }
        return value.equals(obj);
    }

    @Override
    public String toString() {
        return "CacheValue[value: " + value + ", expireTime: " + expireTime + "]";
    }
}
