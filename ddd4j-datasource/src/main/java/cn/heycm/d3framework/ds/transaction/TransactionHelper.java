package cn.heycm.d3framework.ds.transaction;

import cn.heycm.d3framework.core.utils.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 编程事务工具
 * @author heycm
 * @version 1.0
 * @since 2025/8/10 15:11
 */
@Slf4j
public class TransactionHelper {

    private static final int REQUIRED = TransactionDefinition.PROPAGATION_REQUIRED;
    private static final int REQUIRES_NEW = TransactionDefinition.PROPAGATION_REQUIRES_NEW;

    private static PlatformTransactionManager platformTransactionManager;

    public TransactionHelper(PlatformTransactionManager platformTransactionManager) {
        TransactionHelper.platformTransactionManager = platformTransactionManager;
    }

    public static PlatformTransactionManager getPlatformTransactionManager() {
        Assert.notNull(platformTransactionManager, "PlatformTransactionManager not injected.");
        return platformTransactionManager;
    }

    /**
     * 注册事务同步钩子
     * 注册事务钩子当前执行线程必须处于活动事务上下文中
     * @param hooks 钩子函数
     */
    public static void hooks(TransactionSynchronization... hooks) {
        if (hooks != null && hooks.length > 0 && TransactionSynchronizationManager.isSynchronizationActive()) {
            for (TransactionSynchronization hook : hooks) {
                TransactionSynchronizationManager.registerSynchronization(hook);
            }
        }
    }

    /**
     * 开启事务
     * @param runnable    事务单元
     * @param propagation 传播行为
     * @param hooks       钩子函数
     * @return 是否成功
     */
    private static boolean exec(Runnable runnable, int propagation, TransactionSynchronization... hooks) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition(propagation);
        TransactionStatus transaction = getPlatformTransactionManager().getTransaction(definition);
        try {
            runnable.run();
            TransactionHelper.hooks(hooks);
            getPlatformTransactionManager().commit(transaction);
            if (log.isDebugEnabled()) {
                log.debug("Transaction committed.");
            }
            return true;
        } catch (Throwable e) {
            log.error("Transaction rollback because error: {}", e.getMessage(), e);
            getPlatformTransactionManager().rollback(transaction);
        }
        return false;
    }

    /**
     * 以默认传播行为开启事务
     * @param runnable 事务单元
     * @return 是否成功
     */
    public static boolean start(Runnable runnable) {
        return exec(runnable, REQUIRED);
    }

    /**
     * 以 REQUIRED 传播行为开启事务
     * @param runnable 事务单元
     * @return 是否成功
     */
    public static boolean required(Runnable runnable) {
        return exec(runnable, REQUIRED);
    }

    /**
     * 以 REQUIRES_NEW 传播行为开启事务
     * @param runnable 事务单元
     * @return 是否成功
     */
    public static boolean requiresNew(Runnable runnable) {
        return exec(runnable, REQUIRES_NEW);
    }
}
