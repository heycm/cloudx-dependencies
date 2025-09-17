package com.github.heycm.cloudx.lock;

import com.github.heycm.cloudx.lock.aspect.LockAspect;
import com.github.heycm.cloudx.lock.service.LockService;
import com.github.heycm.cloudx.lock.service.LockServiceImpl;
import com.github.heycm.cloudx.lock.util.RedissonUtil;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 锁服务配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/27 22:53
 */
@Configuration
@EnableConfigurationProperties({RedisProperties.class})
public class LockAutoConfiguration {

    /**
     * 单点模式
     */
    @Bean("redissonClient")
    @ConditionalOnProperty(value = "spring.redis.host")
    public RedissonClient standaloneRedissonClient(RedisProperties redisProperties) {
        return RedissonUtil.createStandalone(redisProperties);
    }

    /**
     * 哨兵模式
     */
    @Bean("redissonClient")
    @ConditionalOnProperty(value = "spring.redis.sentinel.nodes")
    public RedissonClient sentinelRedissonClient(RedisProperties redisProperties) {
        return RedissonUtil.createSentinel(redisProperties);
    }

    /**
     * 分片集群
     */
    @Bean("redissonClient")
    @ConditionalOnProperty(value = "spring.redis.cluster.nodes")
    public RedissonClient clusterRedissonClient(RedisProperties redisProperties) {
        return RedissonUtil.createCluster(redisProperties);
    }

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public LockService lockService(@Qualifier("redissonClient") RedissonClient redissonClient) {
        LockService lockService = new LockServiceImpl(redissonClient);
        LockX.setLockService(lockService);
        return lockService;
    }

    @Bean
    @ConditionalOnBean(LockService.class)
    public LockAspect lockAspect(LockService lockService) {
        return new LockAspect(lockService);
    }

}
