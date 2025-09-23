package com.github.heycm.cloudx.core.utils.wrapper;

import com.github.heycm.cloudx.core.context.TenantContextHolder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * 租户上下文包装器，在线程中传递租户上下文
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 18:49
 */
public class TenantContextWrapper {

    private TenantContextWrapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 包装执行单元以传递租户上下文
     * @param runnable 执行单元
     * @return 包装后的执行单元
     */
    public static Runnable wrap(Runnable runnable) {
        String tenantId = TenantContextHolder.getTenantId();
        long mainId = Thread.currentThread().threadId();
        return () -> {
            boolean isSwitch = mainId != Thread.currentThread().threadId();
            if (isSwitch) {
                TenantContextHolder.setTenantId(tenantId);
            }
            try {
                runnable.run();
            } finally {
                if (isSwitch) {
                    TenantContextHolder.clear();
                }
            }
        };
    }

    /**
     * 包装执行单元以传递租户上下文
     * @param supplier 执行单元
     * @return 包装后的执行单元
     */
    public static <U> Supplier<U> wrap(Supplier<U> supplier) {
        String tenantId = TenantContextHolder.getTenantId();
        long mainId = Thread.currentThread().threadId();
        return () -> {
            boolean isSwitch = mainId != Thread.currentThread().threadId();
            if (isSwitch) {
                TenantContextHolder.setTenantId(tenantId);
            }
            try {
                return supplier.get();
            } finally {
                if (isSwitch) {
                    TenantContextHolder.clear();
                }
            }
        };
    }

    /**
     * 包装执行单元以传递租户上下文
     * @param runnable 执行单元
     * @param executor 执行器
     * @return 包装后的执行单元
     */
    public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        return CompletableFuture.runAsync(wrap(runnable), executor);
    }

    /**
     * 包装执行单元以传递租户上下文
     * @param supplier 执行单元
     * @param executor 执行器
     * @return 包装后的执行单元
     */
    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return CompletableFuture.supplyAsync(wrap(supplier), executor);
    }

}
