package com.github.heycm.cloudx.lock;

import com.github.heycm.cloudx.lock.service.LockService;

/**
 * 锁静态工具
 * @author heycm
 * @version 1.0
 * @since 2025/8/27 21:58
 */
public final class LockX {

    public static final long DEFAULT_LEASE_TIME = 3000;
    public static final long DEFAULT_WAIT_TIME = 300;

    private static LockService lockService;

    static void setLockService(LockService lockService) {
        if (lockService == null) {
            throw new IllegalArgumentException("LockService can not be null.");
        }
        LockX.lockService = lockService;
    }

    private static LockService getLockService() {
        if (lockService == null) {
            throw new IllegalStateException("LockService is not injected.");
        }
        return lockService;
    }

    /**
     * 如果获取锁成功则执行任务，否则不执行
     * @param task      任务单元
     * @param key       锁key
     * @param leaseTime 锁有效期
     * @param waitTime  获取锁等待时间
     * @return 是否成功获取锁并运行
     */
    public static boolean runIfLock(Runnable task, String key, long leaseTime, long waitTime) {
        LockService service = getLockService();
        if (service.tryLock(key, leaseTime, waitTime)) {
            try {
                task.run();
            } finally {
                service.release();
            }
            return true;
        }
        return false;
    }

    /**
     * 如果获取锁成功则执行任务，否则不执行
     * @param task 任务单元
     * @param key  锁key
     * @return 是否成功获取锁并运行
     */
    public static boolean runIfLock(Runnable task, String key) {
        return runIfLock(task, key, DEFAULT_LEASE_TIME, DEFAULT_WAIT_TIME);
    }
}
