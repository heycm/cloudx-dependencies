package com.github.heycm.cloudx.uid.shard;

import com.baidu.fsg.uid.utils.DateUtils;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.Assert;

/**
 * 在 {@link com.baidu.fsg.uid.BitsAllocator} 增加了分片基因的位分配器
 * @author heycm
 * @version 1.0
 * @since 2025/10/11 21:29
 */
public class ShardBitsAllocator {

    /**
     * Total 64 bits
     */
    public static final int TOTAL_BITS = 1 << 6;

    /**
     * Bits for [sign-> second-> workId-> shardId-> sequence]
     */
    private int signBits = 1;
    private final int timestampBits;
    private final int workerIdBits;
    private final int shardIdBits;
    private final int sequenceBits;

    /**
     * Max value for workId & shardId & sequence
     */
    private final long maxDeltaSeconds;
    private final long maxWorkerId;
    private final long maxShardId;
    private final long maxSequence;

    /**
     * Shift for timestamp & workerId & shardId
     */
    private final int timestampShift;
    private final int workerIdShift;
    private final int shardIdShift;

    /**
     * Constructor with timestampBits, workerIdBits, shardIdBits, sequenceBits<br>
     * The highest bit used for sign, so <code>63</code> bits for timestampBits, workerIdBits, shardIdBits, sequenceBits
     */
    public ShardBitsAllocator(int timestampBits, int workerIdBits, int shardIdBits, int sequenceBits) {
        // make sure allocated 64 bits
        int allocateTotalBits = signBits + timestampBits + workerIdBits + shardIdBits + sequenceBits;
        Assert.isTrue(allocateTotalBits == TOTAL_BITS, "allocate not enough 64 bits");

        // initialize bits
        this.timestampBits = timestampBits;
        this.workerIdBits = workerIdBits;
        this.shardIdBits = shardIdBits;
        this.sequenceBits = sequenceBits;

        // initialize max value
        this.maxDeltaSeconds = ~(-1L << timestampBits);
        this.maxWorkerId = ~(-1L << workerIdBits);
        this.maxShardId = ~(-1L << shardIdBits);
        this.maxSequence = ~(-1L << sequenceBits);

        // initialize shift
        this.timestampShift = workerIdBits + shardIdBits + sequenceBits;
        this.workerIdShift = shardIdBits + sequenceBits;
        this.shardIdShift = sequenceBits;
    }

    /**
     * Allocate bits for UID according to delta seconds & workerId & shardId & sequence<br>
     * <b>Note that: </b>The highest bit will always be 0 for sign
     *
     * @param deltaSeconds
     * @param workerId
     * @param shardId
     * @param sequence
     * @return
     */
    public long allocate(long deltaSeconds, long workerId, long shardId, long sequence) {
        return (deltaSeconds << timestampShift) | (workerId << workerIdShift) | (shardId << shardIdShift) | sequence;
    }

    /**
     * Getters
     */
    public int getSignBits() {
        return signBits;
    }

    public int getTimestampBits() {
        return timestampBits;
    }

    public int getWorkerIdBits() {
        return workerIdBits;
    }

    public int getShardIdBits() {
        return shardIdBits;
    }

    public int getSequenceBits() {
        return sequenceBits;
    }

    public long getMaxDeltaSeconds() {
        return maxDeltaSeconds;
    }

    public long getMaxWorkerId() {
        return maxWorkerId;
    }

    public long getMaxShardId() {
        return maxShardId;
    }

    public long getMaxSequence() {
        return maxSequence;
    }

    public int getTimestampShift() {
        return timestampShift;
    }

    public int getWorkerIdShift() {
        return workerIdShift;
    }

    public int getShardIdShift() {
        return shardIdShift;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static void main(String[] args) {

        long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(DateUtils.parseByDayPattern("2025-01-01").getTime());

        long currentSec = System.currentTimeMillis() / 1000;

        ShardBitsAllocator allocator = new ShardBitsAllocator(30, 10, 6, 17);

        System.out.println("allocator = " + allocator);

        long currentDeltaSeconds = currentSec - epochSeconds;
        System.out.println("currentDeltaSeconds = " + currentDeltaSeconds);

        long allocate = allocator.allocate(currentDeltaSeconds, 1, 1, 0);

        for (int i = 0; i < 10; i++) {
            long id = allocate + i;
            System.out.println("id  = " + id + ", bit = " + Long.toBinaryString(id));

            long rid = id;
            long mask = allocator.getMaxShardId() << allocator.getShardIdShift();
            rid &= ~mask;
            rid |= ((long) i << allocator.getShardIdShift()) & mask;

            System.out.println("rid = " + rid + ", bit = " + Long.toBinaryString(rid));

        }
    }
}
