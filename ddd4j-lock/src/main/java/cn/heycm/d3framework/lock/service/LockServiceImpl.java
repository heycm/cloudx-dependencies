package cn.heycm.d3framework.lock.service;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 锁服务实现
 * @author heycm
 * @version 1.0
 * @since 2025/8/27 22:00
 */
@Slf4j
public class LockServiceImpl implements LockService {

    public static final String LOCK_KEY_PREFIX = "distributed:lock:";

    private static final ThreadLocal<RLock> LOCK = new ThreadLocal<>();

    private final RedissonClient redissonClient;

    public LockServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean tryLock(String key, long leaseTime, long waitTime) {
        if (leaseTime <= 0) {
            throw new IllegalArgumentException("Lock leaseTime must be greater than 0.");
        }
        if (waitTime <= 0) {
            throw new IllegalArgumentException("Lock waitTime must be greater than 0.");
        }
        try {
            key = LOCK_KEY_PREFIX + key;
            RLock lock = redissonClient.getLock(key);
            LOCK.set(lock);
            boolean success = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if (log.isDebugEnabled()) {
                log.debug("try lock key [{}] result: {}", key, success);
            }
            return success;
        } catch (InterruptedException e) {
            log.error("try lock key [{}] error: {}", key, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void release() {
        RLock lock = LOCK.get();
        if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
            LOCK.remove();
            lock.unlock();
            if (log.isDebugEnabled()) {
                log.debug("lock key [{}] release.", lock.getName());
            }
            return;
        }
        if (lock != null) {
            log.warn("lock key [{}] is invalid, not need to release.", lock.getName());
        }
    }
}
