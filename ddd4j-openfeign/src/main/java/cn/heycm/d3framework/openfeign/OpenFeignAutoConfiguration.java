package cn.heycm.d3framework.openfeign;

import cn.heycm.d3framework.openfeign.interceptor.RequestInterceptor;
import cn.heycm.d3framework.openfeign.interceptor.ResponseInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenFeign配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/27 23:20
 */
@Configuration
public class OpenFeignAutoConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor();
    }

    // @Bean
    public ResponseInterceptor responseInterceptor() {
        return new ResponseInterceptor();
    }
}
