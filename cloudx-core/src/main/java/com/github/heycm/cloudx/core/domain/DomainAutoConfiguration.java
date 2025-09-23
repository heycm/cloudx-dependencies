package com.github.heycm.cloudx.core.domain;

import com.github.heycm.cloudx.core.domain.cqrs.command.AppCommandBusImpl;
import com.github.heycm.cloudx.core.domain.cqrs.command.CommandBus;
import com.github.heycm.cloudx.core.domain.cqrs.query.AppQueryBusImpl;
import com.github.heycm.cloudx.core.domain.cqrs.query.QueryBus;
import com.github.heycm.cloudx.core.domain.event.AsyncDomainEventBridger;
import com.github.heycm.cloudx.core.utils.threadpool.virtual.VirtualThread;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

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

    /**
     * 领域事件调度器，仅负责调度，不负责执行，避免延迟任务错过预期触发时间，桥接给业务线程池执行 <br/>
     * 注意：此线程池队列为无界队列，存在OOM风险
     */
    @Bean("delayedDomainEventScheduler")
    @ConditionalOnMissingBean
    public ThreadPoolTaskScheduler delayedDomainEventScheduler() {
        // 仅负责调度，不负责执行，1个线程足够
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("delayed-event-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(30);
        scheduler.initialize();
        return scheduler;
    }

    /**
     * 领域事件执行器，因为各业务需求不同，因此由业务方配置覆盖当前Bean，若不配置则默认使用虚拟线程执行
     */
    @Bean("asyncDomainEventExecutor")
    @ConditionalOnMissingBean
    public Executor asyncDomainEventExecutor() {
        return VirtualThread.EXECUTOR;
    }

    /**
     * 领域事件异步执行器
     */
    @Bean
    @ConditionalOnBean(name = {"delayedDomainEventScheduler", "asyncDomainEventExecutor"})
    public AsyncDomainEventBridger asyncDomainEventBridger(
            @Qualifier("delayedDomainEventScheduler") ThreadPoolTaskScheduler delayedDomainEventScheduler,
            @Qualifier("asyncDomainEventExecutor") Executor asyncDomainEventExecutor) {
        return new AsyncDomainEventBridger(delayedDomainEventScheduler, asyncDomainEventExecutor);
    }
}
