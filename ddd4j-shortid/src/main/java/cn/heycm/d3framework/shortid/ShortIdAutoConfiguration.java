package cn.heycm.d3framework.shortid;

import cn.heycm.d3framework.shortid.initializer.ShortIdInitializer;
import cn.heycm.d3framework.shortid.service.ShortIdService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 短ID配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/30 22:58
 */
@Configuration
public class ShortIdAutoConfiguration {

    @Bean
    @ConditionalOnBean(JdbcTemplate.class)
    public ShortIdService shortIdService(JdbcTemplate jdbcTemplate) {
        return new ShortIdService(jdbcTemplate);
    }

    @Bean
    @ConditionalOnBean(JdbcTemplate.class)
    @ConditionalOnProperty(name = "ddd4j.shortid.initializer", havingValue = "true")
    public ShortIdInitializer shortIdInitializer(JdbcTemplate jdbcTemplate) {
        ShortIdInitializer initializer = new ShortIdInitializer(jdbcTemplate);
        initializer.initialize();
        return initializer;
    }
}
