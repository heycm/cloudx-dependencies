package cn.heycm.d3framework.core.config;

import cn.heycm.d3framework.core.context.SpringContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Core 包配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/9 18:47
 */
@Configuration
public class CoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpringContext springContext() {
        return new SpringContext();
    }

}
