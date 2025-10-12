package com.github.heycm.cloudx.uid;

import com.github.heycm.cloudx.uid.service.ShardUidService;
import com.github.heycm.cloudx.uid.service.UidService;

/**
 * UID静态工具类
 * @author heycm
 * @version 1.0
 * @since 2025/8/26 23:06
 */
public class UidGenerator {

    private static UidService uidService;
    private static ShardUidService shardUidService;

    static void setUidService(UidService uidService) {
        if (uidService == null) {
            throw new IllegalArgumentException("UidService can not be null.");
        }
        UidGenerator.uidService = uidService;
    }

    static void setShardUidService(ShardUidService shardUidService) {
        if (shardUidService == null) {
            throw new IllegalArgumentException("ShardUidService can not be null.");
        }
        UidGenerator.shardUidService = shardUidService;
    }

    private static UidService getUidService() {
        if (uidService == null) {
            throw new IllegalStateException("UidService is not injected.");
        }
        return uidService;
    }

    private static ShardUidService getShardUidService() {
        if (shardUidService == null) {
            throw new IllegalStateException("ShardUidService is not injected.");
        }
        return shardUidService;
    }

    /**
     * 获取id
     * @return id
     */
    public static long nextId() {
        return getUidService().nextId();
    }

    /**
     * 获取带分片的id
     * @param shardFactor 分片因子
     * @return id
     */
    public static long nextId(long shardFactor) {
        return getShardUidService().nextId(shardFactor);
    }

    /**
     * 提取分片id
     * @param uid id
     * @return 分片id
     */
    public static long extractShardId(long uid) {
        return getShardUidService().extractShardId(uid);
    }
}
