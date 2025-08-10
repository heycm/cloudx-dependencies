package cn.heycm.d3framework.ds.transaction;

import cn.heycm.d3framework.core.contract.exception.ServiceException;
import cn.heycm.d3framework.core.utils.Assert;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 编程事务助手
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
     * 注册事务同步钩子
     * 注册事务钩子当前执行线程必须处于活动事务上下文中
     * @param hooks 钩子函数
     */
    public static void hooks(List<TransactionSynchronization> hooks) {
        if (hooks != null && !hooks.isEmpty() && TransactionSynchronizationManager.isSynchronizationActive()) {
            for (TransactionSynchronization hook : hooks) {
                TransactionSynchronizationManager.registerSynchronization(hook);
            }
        }
    }

    /**
     * 执行事务
     * @param runnable    事务单元
     * @param propagation 传播行为
     * @param hooks       钩子函数
     * @return 是否成功
     */
    private static boolean exec(Runnable runnable, int propagation, List<TransactionSynchronization> hooks) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition(propagation);
        TransactionStatus transaction = getPlatformTransactionManager().getTransaction(definition);
        try {
            runnable.run();
            TransactionHelper.hooks(hooks);
            getPlatformTransactionManager().commit(transaction);
            if (log.isDebugEnabled()) {
                log.debug("TransactionHelper Transaction committed.");
            }
            return true;
        } catch (ServiceException e) {
            getPlatformTransactionManager().rollback(transaction);
        } catch (Throwable e) {
            log.error("TransactionHelper Transaction rollback because error: {}", e.getMessage(), e);
            getPlatformTransactionManager().rollback(transaction);
        }
        return false;
    }

    /**
     * 执行事务
     * @param worker 封装事务执行器
     * @return 是否成功
     */
    public static boolean exec(TransactionWorker worker) {
        boolean exec = TransactionHelper.exec(worker.getTransactionUnit(), worker.getPropagation(), worker.getHooks());
        if (exec) {
            TransactionHelper.callSuccess(worker);
        } else {
            TransactionHelper.callError(worker, new Exception("Transaction rollback"));
        }
        return exec;
    }

    /**
     * 执行事务，错误时抛出异常
     * @param runnable    事务单元
     * @param propagation 传播属性
     * @param hooks       事务钩子
     */
    private static void execThrow(Runnable runnable, int propagation, List<TransactionSynchronization> hooks) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition(propagation);
        TransactionStatus transaction = getPlatformTransactionManager().getTransaction(definition);
        try {
            runnable.run();
            TransactionHelper.hooks(hooks);
            getPlatformTransactionManager().commit(transaction);
            if (log.isDebugEnabled()) {
                log.debug("TransactionHelper Transaction committed.");
            }
        } catch (ServiceException e) {
            getPlatformTransactionManager().rollback(transaction);
            throw e;
        } catch (Throwable e) {
            log.error("TransactionHelper Transaction rollback because error: {}", e.getMessage(), e);
            getPlatformTransactionManager().rollback(transaction);
            throw e;
        }
    }

    /**
     * 执行事务，错误时抛出异常
     * @param worker 封装事务执行器
     */
    public static void execThrow(TransactionWorker worker) {
        try {
            TransactionHelper.execThrow(worker.getTransactionUnit(), worker.getPropagation(), worker.getHooks());
            TransactionHelper.callSuccess(worker);
        } catch (Throwable e) {
            TransactionHelper.callError(worker, e);
            throw e;
        }
    }

    private static void callSuccess(TransactionWorker worker) {
        try {
            if (worker.onSuccess() != null) {
                worker.onSuccess().run();
            }
        } catch (Throwable e) {
            log.error("TransactionWorker.onSuccess() error", e);
        }
    }

    private static void callError(TransactionWorker worker, Throwable e) {
        try {
            if (worker.onError() != null) {
                worker.onError().accept(e);
            }
        } catch (Throwable e1) {
            log.error("TransactionWorker.onError() error", e1);
        }
    }
}
