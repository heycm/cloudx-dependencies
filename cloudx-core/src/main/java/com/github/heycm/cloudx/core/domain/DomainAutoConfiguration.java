package com.github.heycm.cloudx.core.domain;

import com.github.heycm.cloudx.core.domain.cqrs.command.AppCommandBusImpl;
import com.github.heycm.cloudx.core.domain.cqrs.command.CommandBus;
import com.github.heycm.cloudx.core.domain.cqrs.query.AppQueryBusImpl;
import com.github.heycm.cloudx.core.domain.cqrs.query.QueryBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Domain配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/9 18:44
 */
@Configuration
public class DomainAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CommandBus commandBus(ApplicationContext applicationContext) {
        return new AppCommandBusImpl(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public QueryBus queryBus(ApplicationContext applicationContext) {
        return new AppQueryBusImpl(applicationContext);
    }
}
