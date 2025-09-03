package cn.heycm.d3framework.shortid;

import cn.heycm.d3framework.shortid.entity.ShortId;
import cn.heycm.d3framework.shortid.service.ShortIdService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 短ID生成器
 * @author heycm
 * @version 1.0
 * @since 2025/9/3 21:41
 */
public final class ShortIdGenerator {

    private static final Map<String, AtomicInteger> NEXT_ID = new ConcurrentHashMap<>();
    private static final Map<String, Integer> MAX_ID = new ConcurrentHashMap<>();
    private static final Map<String, Lock> LOCKS = new ConcurrentHashMap<>();

    private static ShortIdService shortIdService;

    static void setShortIdService(ShortIdService shortIdService) {
        ShortIdGenerator.shortIdService = shortIdService;
    }

    private static ShortIdService getShortIdService() {
        if (shortIdService == null) {
            throw new IllegalStateException("ShortIdService is not injected.");
        }
        return shortIdService;
    }

    /**
     * 获取下一个ID
     * @param idKey ID标识
     * @return 下一个ID
     */
    public static int nextId(String idKey) {
        AtomicInteger nextId = NEXT_ID.get(idKey);
        Integer maxId = MAX_ID.get(idKey);
        if (nextId == null || nextId.get() >= maxId) {
            LOCKS.putIfAbsent(idKey, new ReentrantLock());
            Lock lock = LOCKS.computeIfAbsent(idKey, k -> new ReentrantLock());
            try {
                lock.lock();
                nextId = NEXT_ID.get(idKey);
                maxId = MAX_ID.get(idKey);
                if (nextId == null || nextId.get() >= maxId) {
                    ShortId shortId = getShortIdService().acquireId(idKey);
                    nextId = new AtomicInteger(shortId.getNextId());
                    maxId = shortId.getMaxId();
                    NEXT_ID.put(idKey, nextId);
                    MAX_ID.put(idKey, maxId);
                }
            } finally {
                lock.unlock();
            }
        }
        return nextId.getAndIncrement();
    }
}
