package cn.heycm.d3framework.redis.client;

import cn.heycm.d3framework.core.context.TenantContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.StringUtils;

/**
 * 多租户 RedisClient，默认使用租户ID作为前缀
 * @author heycm
 * @since 2025/8/25 21:34
 */
public class TenantRedisClientImpl extends RedisClientImpl {

    public TenantRedisClientImpl(RedisTemplate<String, Object> redisTemplate, RedisMessageListenerContainer listenerContainer) {
        super(redisTemplate, listenerContainer);
    }

    @Override
    protected String wrapKey(String key) {
        String tenantId = TenantContextHolder.getTenantId();
        return StringUtils.hasText(tenantId) ? tenantId + ":" + key : key;
    }
}
