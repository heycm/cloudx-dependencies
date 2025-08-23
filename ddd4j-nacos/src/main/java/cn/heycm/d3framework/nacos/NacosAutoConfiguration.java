package cn.heycm.d3framework.nacos;

import cn.heycm.d3framework.nacos.conf.NacosConf;
import cn.heycm.d3framework.nacos.service.NacosConfListener;
import cn.heycm.d3framework.nacos.service.NacosConfPublisher;
import cn.heycm.d3framework.nacos.service.impl.NacosConfListenerImpl;
import cn.heycm.d3framework.nacos.service.impl.NacosConfPublisherImpl;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Nacos自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/25 21:02
 */
@Configuration
@EnableConfigurationProperties({NacosConf.class})
@Slf4j
public class NacosAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {ConfigService.class})
    public ConfigService configService(NacosConfigProperties nacosConfigProperties) throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosConfigProperties.getServerAddr());
        if (StringUtils.hasText(nacosConfigProperties.getNamespace())) {
            properties.put(PropertyKeyConst.NAMESPACE, nacosConfigProperties.getNamespace());
        }
        if (StringUtils.hasText(nacosConfigProperties.getUsername()) && StringUtils.hasText(nacosConfigProperties.getPassword())) {
            properties.put(PropertyKeyConst.USERNAME, nacosConfigProperties.getUsername());
            properties.put(PropertyKeyConst.PASSWORD, nacosConfigProperties.getPassword());
        }
        return NacosFactory.createConfigService(properties);
    }

    @Bean
    public NacosConfListener nacosConfListener(ConfigService configService) {
        return new NacosConfListenerImpl(configService);
    }

    @Bean
    public NacosConfPublisher nacosConfPublisher(ConfigService configService) {
        return new NacosConfPublisherImpl(configService);
    }
}
