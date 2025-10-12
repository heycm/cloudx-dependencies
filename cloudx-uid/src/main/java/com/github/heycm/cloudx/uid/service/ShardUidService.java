package com.github.heycm.cloudx.uid.service;

/**
 * 带分片基因的UID服务
 * @author heycm
 * @version 1.0
 * @since 2025/10/12 21:48
 */
public interface ShardUidService {

    /**
     * 生成ID
     * @param shardFactor 分片因子
     * @return ID
     */
    long nextId(long shardFactor);

    /**
     * 获取分片ID
     * @param uid UID
     * @return 分片ID
     */
    long extractShardId(long uid);
}
