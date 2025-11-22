package com.github.heycm.cloudx.sensitive;

import com.github.heycm.cloudx.sensitive.properties.SensitiveWordProperties;
import com.github.heycm.cloudx.sensitive.service.SensitiveWordRepository;
import com.github.heycm.cloudx.sensitive.service.SensitiveWordService;
import com.github.heycm.cloudx.sensitive.service.filter.SensitiveWordFilter;
import com.github.heycm.cloudx.sensitive.service.filter.SensitiveWordFilterRefresher;
import com.github.heycm.cloudx.sensitive.service.impl.EmptySensitiveWordRepository;
import com.github.heycm.cloudx.sensitive.service.impl.SensitiveWordServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 敏感词组件配置
 * @author heycm
 * @version 1.0
 * @since 2025/11/22 14:00
 */
@Configuration
@ConditionalOnProperty(prefix = "cloudx.sensitive.filter", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SensitiveWordProperties.class)
@Slf4j
public class SensitiveWordAutoConfiguration {

    private final SensitiveWordProperties properties;

    public SensitiveWordAutoConfiguration(SensitiveWordProperties sensitiveWordProperties) {
        this.properties = sensitiveWordProperties;
        log.info("初始化敏感词检测组件");
    }

    /**
     * 注册空敏感词词库，词库需要应用服务自行实现，以覆盖默认空实现
     */
    @Bean
    @ConditionalOnMissingBean
    public SensitiveWordRepository sensitiveWordRepository() {
        return new EmptySensitiveWordRepository();
    }

    /**
     * 注册敏感词过滤器
     */
    @Bean
    @ConditionalOnBean(SensitiveWordRepository.class)
    public SensitiveWordFilter sensitiveWordFilter() {
        return new SensitiveWordFilter();
    }

    /**
     * 注册敏感词过滤器刷新器
     */
    @Bean
    public SensitiveWordFilterRefresher sensitiveWordFilterRefresher(SensitiveWordRepository sensitiveWordRepository,
            SensitiveWordFilter sensitiveWordFilter) {
        return new SensitiveWordFilterRefresher(sensitiveWordRepository, sensitiveWordFilter, properties.getRefreshIntervalMs());
    }

    /**
     * 注册敏感词服务
     */
    @Bean
    public SensitiveWordService sensitiveWordService(SensitiveWordFilter sensitiveWordFilter,
            SensitiveWordFilterRefresher sensitiveWordFilterRefresher, SensitiveWordRepository sensitiveWordRepository) {
        return new SensitiveWordServiceImpl(sensitiveWordFilter, sensitiveWordFilterRefresher, sensitiveWordRepository);
    }
}
