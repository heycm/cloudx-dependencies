package cn.heycm.d3framework.localcache;

import cn.heycm.d3framework.localcache.cache.CaffeineCacheImpl;
import cn.heycm.d3framework.localcache.cache.LocalCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * 本地缓存配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/23 15:23
 */
@Configurable
@Slf4j
public class LocalCacheAutoConfiguration {

    @Value("${ddd4j.local-cache.maximumSize:10000}")
    private long maximumSize;

    public LocalCacheAutoConfiguration() {
        log.info("LocalCache init...");
    }

    @Bean
    public LocalCache localCache() {
        return new CaffeineCacheImpl(maximumSize);
    }
}
