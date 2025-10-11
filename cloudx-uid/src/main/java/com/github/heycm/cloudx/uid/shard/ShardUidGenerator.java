package com.github.heycm.cloudx.uid.shard;

import com.baidu.fsg.uid.exception.UidGenerateException;

/**
 * ID生成器接口
 * @author heycm
 * @version 1.0
 * @since 2025/10/11 22:08
 */
public interface ShardUidGenerator {

    /**
     * 获取下一个ID
     * @param shardFactor 分片因子，用于计算分片id
     * @return UID
     * @throws UidGenerateException
     */
    long getUID(long shardFactor) throws UidGenerateException;

    /**
     * 解析UID各片段的值
     * Such as timestamp & workerId & shardId & sequence...
     * @param uid UID
     * @return
     */
    String parseUID(long uid);

    /**
     * 提取UID的分片id
     * @param uid UID
     * @return
     */
    long extractShardId(long uid);
}
