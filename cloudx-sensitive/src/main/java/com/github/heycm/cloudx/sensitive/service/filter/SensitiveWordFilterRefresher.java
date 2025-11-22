package com.github.heycm.cloudx.sensitive.service.filter;

import com.github.heycm.cloudx.sensitive.service.SensitiveWordRepository;
import java.time.Duration;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 敏感词过滤器刷新器
 * @author heycm
 * @version 1.0
 * @since 2025/11/22 22:15
 */
@Slf4j
public class SensitiveWordFilterRefresher {

    /**
     * 敏感词词库
     */
    private final SensitiveWordRepository repository;

    /**
     * 敏感词过滤器
     */
    private final SensitiveWordFilter filter;

    /**
     * 刷新间隔（毫秒）
     */
    private int refreshInterval;

    /**
     * 刷新任务调度器
     */
    private ThreadPoolTaskScheduler scheduler;

    public SensitiveWordFilterRefresher(SensitiveWordRepository repository, SensitiveWordFilter filter, int refreshInterval) {
        this.repository = repository;
        this.filter = filter;
        this.refreshInterval = refreshInterval;
        this.initSheduler();
        // boot init.
        this.refresh();
    }

    /**
     * 刷新敏感词过滤器
     */
    public synchronized void refresh() {
        try {
            log.info("statr refresh sensitive word...");
            // 读写分离
            SensitiveWordFilter f = new SensitiveWordFilter();
            repository.foreach(f::addWord);
            filter.reset(f);
            log.info("finish refresh sensitive word.");
        } catch (Exception e) {
            log.error("refresh sensitive word error", e);
        }
    }

    private void initSheduler() {
        if (refreshInterval <= 0) {
            return;
        }
        if (refreshInterval < 60 * 1000) {
            refreshInterval = 60 * 1000;
            log.warn("refreshInterval is too small, set to {}ms", refreshInterval);
        }
        scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("swf-refresher");
        scheduler.setAwaitTerminationSeconds(30);
        scheduler.initialize();
        scheduler.scheduleAtFixedRate(this::refresh, Instant.now(), Duration.ofMillis(refreshInterval));
    }
}
