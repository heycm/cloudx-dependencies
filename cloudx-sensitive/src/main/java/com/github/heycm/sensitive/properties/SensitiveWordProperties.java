package com.github.heycm.sensitive.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 组件配置属性
 * @author heycm
 * @version 1.0
 * @since 2025/11/22 14:02
 */
@Data
@ConfigurationProperties(prefix = "cloudx.sensitive.filter")
public class SensitiveWordProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 敏感词刷新间隔，单位秒
     */
    private int refreshInterval = 60;

    public int getRefreshIntervalMs() {
        return refreshInterval * 1000;
    }
}
