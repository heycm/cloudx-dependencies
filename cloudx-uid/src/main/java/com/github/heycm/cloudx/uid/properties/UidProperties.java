package com.github.heycm.cloudx.uid.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * UID 配置，支持配置雪花ID各部分位数，可配置共63位，首位符号位为0不用配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/26 22:11
 */
@ConfigurationProperties(prefix = "cloudx.uid.worker-id")
public class UidProperties {

    /**
     * 机器ID，要求全局唯一
     */
    private int workerId;

    /**
     * 时间位，最大可表示秒数，默认 30 位，可使用 2^30 秒约为 34 年
     */
    private int timeBits = 30;

    /**
     * 机器位，最大可表示机器数，默认 14 位，可支持 2^14=16384 台机器使用
     */
    private int workerBits = 14;

    /**
     * 分片位，最大可表示分片数，默认 0 位，不分片
     */
    private int shardBits = 0;

    /**
     * 序列位，同一秒内单节点可生成的ID数，默认 19 位，可支持 2^19=524288 个ID，按默认配置理论上全机器每秒支持并发量达 16384*524288≈85.9亿
     */
    private int seqBits = 19;

    /**
     * 起始日期，默认 2025-01-01
     */
    private String epochStr = "2025-01-01";

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public int getTimeBits() {
        return timeBits;
    }

    public void setTimeBits(int timeBits) {
        this.timeBits = timeBits;
    }

    public int getWorkerBits() {
        return workerBits;
    }

    public void setWorkerBits(int workerBits) {
        this.workerBits = workerBits;
    }

    public int getShardBits() {
        return shardBits;
    }

    public void setShardBits(int shardBits) {
        this.shardBits = shardBits;
    }

    public int getSeqBits() {
        return seqBits;
    }

    public void setSeqBits(int seqBits) {
        this.seqBits = seqBits;
    }

    public String getEpochStr() {
        return epochStr;
    }

    public void setEpochStr(String epochStr) {
        this.epochStr = epochStr;
    }
}
