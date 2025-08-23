package cn.heycm.d3framework.ds;

import cn.heycm.d3framework.ds.aspect.TenantAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 多租户数据源配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/23 23:26
 */
@Configuration
@ConditionalOnClass(DispatcherServlet.class)
@ConditionalOnProperty(name = "ddd4j.datasource.tenant", havingValue = "true")
public class TenantDataSourceAutoConfiguration {

    @Bean
    public TenantAspect tenantAspect() {
        return new TenantAspect();
    }

}
