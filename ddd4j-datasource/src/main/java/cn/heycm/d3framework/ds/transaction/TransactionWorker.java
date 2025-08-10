package cn.heycm.d3framework.ds.transaction;

import java.util.List;
import java.util.function.Consumer;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;

/**
 * 事务执行器
 * @author heycm
 * @version 1.0
 * @since 2025/8/10 15:38
 */
public interface TransactionWorker {

    /**
     * 事务传播级别，默认为 PROPAGATION_REQUIRED
     */
    default int getPropagation() {
        return TransactionDefinition.PROPAGATION_REQUIRED;
    }

    /**
     * 执行事务单元
     */
    Runnable getTransactionUnit();

    /**
     * 事务钩子
     */
    default List<TransactionSynchronization> getHooks() {
        return null;
    }

    /**
     * 事务成功回调
     */
    default Runnable onSuccess() {
        return null;
    }

    /**
     * 事务失败回调
     */
    default Consumer<Throwable> onError() {
        return null;
    }
}
