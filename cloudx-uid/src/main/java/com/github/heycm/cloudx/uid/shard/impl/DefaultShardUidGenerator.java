package com.github.heycm.cloudx.uid.shard.impl;

import com.baidu.fsg.uid.exception.UidGenerateException;
import com.baidu.fsg.uid.utils.DateUtils;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import com.github.heycm.cloudx.uid.shard.ShardBitsAllocator;
import com.github.heycm.cloudx.uid.shard.ShardUidGenerator;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 在 {@link com.baidu.fsg.uid.impl.DefaultUidGenerator} 的基础上加了分片位的ID生成器
 * @author heycm
 * @version 1.0
 * @since 2025/10/11 22:58
 */
public class DefaultShardUidGenerator implements ShardUidGenerator, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultShardUidGenerator.class);

    /** Bits allocate */
    protected int timeBits = 28;
    protected int workerBits = 18;
    protected int shardBits = 4;
    protected int seqBits = 13;

    /** Customer epoch, unit as second. For example 2016-05-20 (ms: 1463673600000)*/
    protected String epochStr = "2016-05-20";
    protected long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1463673600000L);

    /** Stable fields after spring bean initializing */
    protected ShardBitsAllocator shardBitsAllocator;
    protected long workerId;

    /** Volatile fields caused by nextId() */
    protected long sequence = 0L;
    protected long lastSecond = -1L;

    /** Spring property */
    protected WorkerIdAssigner workerIdAssigner;

    @Override
    public void afterPropertiesSet() throws Exception {
        // initialize bits allocator
        shardBitsAllocator = new ShardBitsAllocator(timeBits, workerBits, shardBits, seqBits);

        // initialize worker id
        workerId = workerIdAssigner.assignWorkerId();
        if (workerId > shardBitsAllocator.getMaxWorkerId()) {
            throw new RuntimeException("Worker id " + workerId + " exceeds the max " + shardBitsAllocator.getMaxWorkerId());
        }

        LOGGER.info("Initialized bits(1, {}, {}, {}, {}) for workerID:{}", timeBits, workerBits, shardBits, seqBits, workerId);
    }

    @Override
    public long getUID(long shardFactor) throws UidGenerateException {
        try {
            return nextId(shardFactor);
        } catch (Exception e) {
            LOGGER.error("Generate unique id exception. ", e);
            throw new UidGenerateException(e);
        }
    }

    protected synchronized long nextId(long shardFactor) {
        long currentSecond = getCurrentSecond();

        // Clock moved backwards, refuse to generate uid
        if (currentSecond < lastSecond) {
            long refusedSeconds = lastSecond - currentSecond;
            throw new UidGenerateException("Clock moved backwards. Refusing for %d seconds", refusedSeconds);
        }

        // At the same second, increase sequence
        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) & shardBitsAllocator.getMaxSequence();
            // Exceed the max sequence, we wait the next second to generate uid
            if (sequence == 0) {
                currentSecond = getNextSecond(lastSecond);
            }

            // At the different second, sequence restart from zero
        } else {
            sequence = 0L;
        }

        lastSecond = currentSecond;

        // Allocate bits for UID
        long shardId = calculateShardId(shardFactor);
        return shardBitsAllocator.allocate(currentSecond - epochSeconds, workerId, shardId, sequence);
    }

    protected long calculateShardId(long shardFactor) {
        /*
         * 计算分片id：shardFactor & maxShardId 等价于 shardFactor % 2^shardBits
         * 假设 shardBits = 4 -> shardFactor & (2^4 - 1) == shardFactor % 16，就是用分片因子对总分片数量取模
         */
        return shardFactor & shardBitsAllocator.getMaxShardId();
    }

    @Override
    public long getUID() throws UidGenerateException {
        return getUID(0L);
    }

    @Override
    public String parseUID(long uid) {
        long totalBits = ShardBitsAllocator.TOTAL_BITS;
        long signBits = shardBitsAllocator.getSignBits();
        long timestampBits = shardBitsAllocator.getTimestampBits();
        long workerIdBits = shardBitsAllocator.getWorkerIdBits();
        int shardIdBits = shardBitsAllocator.getShardIdBits();
        long sequenceBits = shardBitsAllocator.getSequenceBits();

        // parse UID
        long sequence = (uid << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        long shardId = (uid << (workerIdBits + timestampBits + signBits)) >>> (totalBits - shardIdBits);
        long workerId = (uid << (timestampBits + signBits)) >>> (totalBits - workerIdBits);
        long deltaSeconds = uid >>> (workerIdBits + shardIdBits + sequenceBits);

        Date thatTime = new Date(TimeUnit.SECONDS.toMillis(epochSeconds + deltaSeconds));
        String thatTimeStr = DateUtils.formatByDateTimePattern(thatTime);

        // format as string
        return String.format("{\"UID\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"shardId\":\"%d\",\"sequence\":\"%d\"}",
                uid, thatTimeStr, workerId, shardId, sequence);
    }

    @Override
    public long extractShardId(long uid) {
        long totalBits = ShardBitsAllocator.TOTAL_BITS;
        long signBits = shardBitsAllocator.getSignBits();
        long timestampBits = shardBitsAllocator.getTimestampBits();
        long workerIdBits = shardBitsAllocator.getWorkerIdBits();
        int shardIdBits = shardBitsAllocator.getShardIdBits();
        return (uid << (workerIdBits + timestampBits + signBits)) >>> (totalBits - shardIdBits);
    }

    /**
     * Get next millisecond
     */
    private long getNextSecond(long lastTimestamp) {
        long timestamp = getCurrentSecond();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentSecond();
        }

        return timestamp;
    }

    /**
     * Get current second
     */
    private long getCurrentSecond() {
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (currentSecond - epochSeconds > shardBitsAllocator.getMaxDeltaSeconds()) {
            throw new UidGenerateException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond);
        }

        return currentSecond;
    }

    /**
     * Setters for spring property
     */
    public void setWorkerIdAssigner(WorkerIdAssigner workerIdAssigner) {
        this.workerIdAssigner = workerIdAssigner;
    }

    public void setTimeBits(int timeBits) {
        if (timeBits > 0) {
            this.timeBits = timeBits;
        }
    }

    public void setWorkerBits(int workerBits) {
        if (workerBits > 0) {
            this.workerBits = workerBits;
        }
    }

    public void setShardBits(int shardBits) {
        if (shardBits > 0) {
            this.shardBits = shardBits;
        }
    }

    public void setSeqBits(int seqBits) {
        if (seqBits > 0) {
            this.seqBits = seqBits;
        }
    }

    public void setEpochStr(String epochStr) {
        if (StringUtils.isNotBlank(epochStr)) {
            this.epochStr = epochStr;
            this.epochSeconds = TimeUnit.MILLISECONDS.toSeconds(DateUtils.parseByDayPattern(epochStr).getTime());
        }
    }
}
