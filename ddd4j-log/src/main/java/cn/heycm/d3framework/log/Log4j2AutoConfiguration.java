package cn.heycm.d3framework.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Log4j2 自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 15:21
 */
@Configuration
public class Log4j2AutoConfiguration {

    public static final Logger LOGGER = LoggerFactory.getLogger(Log4j2AutoConfiguration.class);

    /**
     * Log4j2 支持 SpringBoot 环境变量读取，通过 ${spring:xxx} 读取系统环境变量
     * 默认配置文件名称 log4j2.xml 读取时机早于 Spring 环境初始化，因此需要重命名为 log4j2-spring.xml 或指定 logging.config
     * </p>
     * 参考文档：
     * https://logging.apache.org/log4j/2.x/log4j-spring-boot.html
     * https://docs.spring.io/spring-boot/reference/features/logging.html#features.logging.log4j2-extensions.environment-properties-lookup
     */
    public Log4j2AutoConfiguration() {
        LOGGER.info("组件 [Log4j2] 启动...");
    }
}
