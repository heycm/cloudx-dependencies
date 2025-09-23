package com.github.heycm.cloudx.core.domain.event;

import com.github.heycm.cloudx.core.context.TenantContextHolder;
import java.time.Instant;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 领域事件桥接器
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 17:13
 */
@Slf4j
public class AsyncDomainEventBridger {

    /**
     * 调度器
     */
    private final ThreadPoolTaskScheduler scheduler;

    /**
     * 执行器
     */
    private final Executor executor;

    public AsyncDomainEventBridger(ThreadPoolTaskScheduler scheduler, Executor executor) {
        this.scheduler = scheduler;
        this.executor = executor;
    }

    /**
     * 延迟触发事件
     * @param event 领域事件
     * @param delay 延迟时间（毫秒）
     */
    public void schedule(DomainEvent event, long delay) {
        assert event != null;
        assert delay > 0;
        if (log.isDebugEnabled()) {
            log.debug("add delayed event will be triggered after [{}] delay {}ms.", event, delay);
        }
        scheduler.schedule(() -> {
            if (log.isDebugEnabled()) {
                log.debug("trigger delayed event: {}", event);
            }
            executor.execute(() -> {
                try {
                    TenantContextHolder.setTenantId(event.getTenantId());
                    event.publish();
                } finally {
                    TenantContextHolder.clear();
                }
            });
        }, Instant.now().plusMillis(delay));
    }

    /**
     * 异步触发事件
     * @param event 领域事件
     */
    public void async(DomainEvent event) {
        assert event != null;
        executor.execute(() -> {
            try {
                TenantContextHolder.setTenantId(event.getTenantId());
                event.publish();
            } finally {
                TenantContextHolder.clear();
            }
        });
    }
}
