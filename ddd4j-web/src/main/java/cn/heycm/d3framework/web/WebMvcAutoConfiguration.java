package cn.heycm.d3framework.web;

import cn.heycm.d3framework.core.context.SpringContext;
import cn.heycm.d3framework.web.filter.CipherFilter;
import cn.heycm.d3framework.web.filter.RequestCacheFilter;
import cn.heycm.d3framework.web.filter.RequestLoggingFilter;
import cn.heycm.d3framework.web.filter.TraceFilter;
import cn.heycm.d3framework.web.session.AbstractSessionResolver;
import java.util.Collection;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/9 21:05
 */
@Configuration
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        Collection<AbstractSessionResolver> beans = SpringContext.getBeans(AbstractSessionResolver.class);
        resolvers.addAll(beans);
    }

    /**
     * 注册链路跟踪过滤器
     */
    @Bean
    @Order(Integer.MIN_VALUE)
    public TraceFilter traceFilter() {
        return new TraceFilter();
    }

    /**
     * 注册请求缓存过滤器（可重复读IO）
     */
    @Bean
    @Order(0)
    public RequestCacheFilter requestCacheFilter() {
        return new RequestCacheFilter();
    }

    /**
     * 注册请求响应日志自动打印过滤器
     */
    @Bean
    @Order(1)
    public RequestLoggingFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }

    /**
     * 注册请求参数加解密过滤器
     */
    // @Bean
    // @Order(2)
    public CipherFilter cipherFilter() {
        return new CipherFilter();
    }
}
