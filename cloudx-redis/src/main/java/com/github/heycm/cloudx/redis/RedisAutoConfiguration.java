package com.github.heycm.cloudx.redis;

import com.github.heycm.cloudx.redis.aspect.LimiterAspect;
import com.github.heycm.cloudx.redis.client.RedisClient;
import com.github.heycm.cloudx.redis.client.TenantRedisClientImpl;
import com.github.heycm.cloudx.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Redis自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/25 21:09
 */
@Configuration
// @EnableConfigurationProperties({RedisProperties.class})
@AutoConfigureAfter(org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class)
@Slf4j
public class RedisAutoConfiguration {

    // /**
    //  * 单点模式
    //  */
    // @Bean(name = "redisConnectionFactory")
    // @ConditionalOnProperty(value = "spring.data.redis.host")
    // public RedisConnectionFactory standaloneRedisConnectionFactory(RedisProperties redisProperties) {
    //     JedisClientConfiguration jedisConf = RedisUtil.createJedisConf(redisProperties);
    //     RedisStandaloneConfiguration standaloneConf = RedisUtil.createStandaloneConf(redisProperties);
    //     return new JedisConnectionFactory(standaloneConf, jedisConf);
    // }
    //
    // /**
    //  * 哨兵集群
    //  */
    // @Bean(name = "redisConnectionFactory")
    // @ConditionalOnProperty(value = "spring.data.redis.sentinel.nodes")
    // public RedisConnectionFactory sentinelRedisConnectionFactory(RedisProperties redisProperties) {
    //     JedisClientConfiguration jedisConf = RedisUtil.createJedisConf(redisProperties);
    //     RedisSentinelConfiguration sentinelConf = RedisUtil.createSentinelConf(redisProperties);
    //     return new JedisConnectionFactory(sentinelConf, jedisConf);
    // }
    //
    // /**
    //  * 分片集群
    //  */
    // @Bean(name = "redisConnectionFactory")
    // @ConditionalOnProperty(value = "spring.data.redis.cluster.nodes")
    // public RedisConnectionFactory clusterRedisConnectionFactory(RedisProperties redisProperties) {
    //     JedisClientConfiguration jedisConf = RedisUtil.createJedisConf(redisProperties);
    //     RedisClusterConfiguration clusterConf = RedisUtil.createClusterConf(redisProperties);
    //     return new JedisConnectionFactory(clusterConf, jedisConf);
    // }

    @Bean
    @ConditionalOnClass(RedisConnectionFactory.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return RedisUtil.createRedisTemplate(redisConnectionFactory);
    }

    @Bean
    @ConditionalOnClass(RedisConnectionFactory.class)
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisClient redisClient(RedisTemplate<String, Object> redisTemplate, RedisMessageListenerContainer redisMessageListenerContainer) {
        return new TenantRedisClientImpl(redisTemplate, redisMessageListenerContainer);
    }

    @Bean
    @ConditionalOnBean(RedisClient.class)
    public LimiterAspect limiterAspect(RedisClient redisClient) {
        return new LimiterAspect(redisClient);
    }

}
