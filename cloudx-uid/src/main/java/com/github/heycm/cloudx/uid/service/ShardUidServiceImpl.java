package com.github.heycm.cloudx.uid.service;

import com.github.heycm.cloudx.uid.shard.ShardUidGenerator;

/**
 * 分片UID服务
 * @author heycm
 * @version 1.0
 * @since 2025/10/12 21:50
 */
public class ShardUidServiceImpl implements ShardUidService {

    private final ShardUidGenerator shardUidGenerator;

    public ShardUidServiceImpl(ShardUidGenerator shardUidGenerator) {
        this.shardUidGenerator = shardUidGenerator;
    }

    @Override
    public long nextId(long shardFactor) {
        return shardUidGenerator.getUID(shardFactor);
    }

    @Override
    public long extractShardId(long uid) {
        return shardUidGenerator.extractShardId(uid);
    }
}
