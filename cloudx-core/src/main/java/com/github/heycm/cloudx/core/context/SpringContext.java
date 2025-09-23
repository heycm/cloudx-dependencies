package com.github.heycm.cloudx.core.context;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Spring 上下文
 * @author heycm
 * @version 1.0
 * @since 2025/8/1 23:18
 */
@Slf4j
@Order(PriorityOrdered.HIGHEST_PRECEDENCE) // 优先级最高，最先初始化
public class SpringContext implements ApplicationContextAware, ApplicationRunner {

    /**
     * 应用启动完成信号
     */
    public static final CountDownLatch APP_STARTED_SIGNAL = new CountDownLatch(1);

    /**
     * Spring 上下文初始化完成信号
     */
    public static final CountDownLatch CONTEXT_STARTED_SIGNAL = new CountDownLatch(1);

    private static ApplicationContext APPLICATION_CONTEXT;

    public SpringContext() {
        log.info("SpringContext loading...");
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getMethodName().equals("main")) {
                try {
                    String projectName = Class.forName(element.getClassName()).getPackage().getName();
                    log.info("ProjectName: {}", projectName);
                } catch (ClassNotFoundException e) {
                    log.error("Cannot find class: {}", element.getClassName());
                }
            }
            break;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.APPLICATION_CONTEXT = applicationContext;
        CONTEXT_STARTED_SIGNAL.countDown();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // APP 启动完成信号
        APP_STARTED_SIGNAL.countDown();
    }

    @SneakyThrows
    public static ApplicationContext getApplicationContext() {
        if (APPLICATION_CONTEXT == null) {
            CONTEXT_STARTED_SIGNAL.await();
        }
        return APPLICATION_CONTEXT;
    }

    public static <T> T getBean(@NonNull Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(@NonNull String name) {
        return (T) getApplicationContext().getBean(name);
    }

    public static <T> T getBean(@NonNull String name, @NonNull Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static <T> Collection<T> getBeans(@NonNull Class<T> clazz) {
        Map<String, T> beansOfType = getApplicationContext().getBeansOfType(clazz);
        if (beansOfType.isEmpty()) {
            return Collections.emptyList();
        }
        return beansOfType.values();
    }

    @SneakyThrows
    public static <T> T getBeanAwait(@NonNull Class<T> clazz) {
        APP_STARTED_SIGNAL.await();
        return getApplicationContext().getBean(clazz);
    }

    public static Environment getEnv() {
        return getApplicationContext().getEnvironment();
    }

    /**
     * 发布事件
     * @param event 事件
     */
    public static void publish(ApplicationEvent event) {
        getApplicationContext().publishEvent(event);
    }
}
