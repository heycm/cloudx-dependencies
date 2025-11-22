package com.github.heycm.sensitive;

import com.github.heycm.sensitive.properties.SensitiveWordProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
}
