package com.github.heycm.cloudx.core.utils.wrapper;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import org.slf4j.MDC;

/**
 * MDC包装器，线程上下文传递
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 18:52
 */
public class MdcWrapper {

    private MdcWrapper() {
        throw new UnsupportedOperationException("Utility class");
    }


    /**
     * 包装执行单元以传递MDC上下文
     * @param runnable 执行单元
     * @return 包装后的执行单元
     */
    public static Runnable wrap(Runnable runnable) {
        Map<String, String> ctx = MDC.getCopyOfContextMap();
        long mainId = Thread.currentThread().threadId();
        return () -> {
            boolean isSwitch = mainId != Thread.currentThread().threadId();
            if (isSwitch) {
                MDC.setContextMap(ctx);
            }
            try {
                runnable.run();
            } finally {
                if (isSwitch) {
                    MDC.clear();
                }
            }
        };
    }

    /**
     * 包装执行单元以传递MDC上下文
     * @param supplier 执行单元
     * @return 包装后的执行单元
     */
    public static <U> Supplier<U> wrap(Supplier<U> supplier) {
        Map<String, String> ctx = MDC.getCopyOfContextMap();
        long mainId = Thread.currentThread().threadId();
        return () -> {
            boolean isSwitch = mainId != Thread.currentThread().threadId();
            if (isSwitch) {
                MDC.setContextMap(ctx);
            }
            try {
                return supplier.get();
            } finally {
                if (isSwitch) {
                    MDC.clear();
                }
            }
        };
    }

    /**
     * 创建一个异步任务，并传递 MDC
     * @param runnable 执行单元
     * @param executor 执行器
     * @return CompletableFuture
     */
    public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        return CompletableFuture.runAsync(wrap(runnable), executor);
    }

    /**
     * 创建一个异步任务，并传递 MDC
     * @param supplier 执行单元
     * @param executor 执行器
     * @return CompletableFuture
     */
    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return CompletableFuture.supplyAsync(wrap(supplier), executor);
    }
}
