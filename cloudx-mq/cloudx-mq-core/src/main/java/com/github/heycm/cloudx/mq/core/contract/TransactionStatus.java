package com.github.heycm.cloudx.mq.core.contract;

import lombok.Getter;

/**
 * 事务消息时，本地事务状态
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 20:30
 */
@Getter
public enum TransactionStatus {

    /**
     * 未知
     */
    UNKNOWN(0),

    /**
     * 已提交
     */
    COMMIT(1),

    /**
     * 已回滚
     */
    ROLLBACK(2);

    private final int value;

    TransactionStatus(int value) {
        this.value = value;
    }
}
