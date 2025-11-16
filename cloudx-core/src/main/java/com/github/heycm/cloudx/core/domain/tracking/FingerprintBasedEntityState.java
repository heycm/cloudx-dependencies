package com.github.heycm.cloudx.core.domain.tracking;

/**
 * 基于指纹的实体状态快照实现
 * @author heycm
 * @version 1.0
 * @since 2025/11/16 14:44
 */
public class FingerprintBasedEntityState implements EntityState {

    /**
     * 实体对象
     */
    private final TrackableEntity entity;

    /**
     * 状态指纹计算器
     */
    private final FingerprintCalculator fingerprintCalculator;

    /**
     * 是否是新增的实体
     */
    private final boolean isNew;

    /**
     * 原始状态指纹
     */
    private final int originalFingerprint;

    public FingerprintBasedEntityState(TrackableEntity entity, FingerprintCalculator fingerprintCalculator, boolean isNew) {
        this.entity = entity;
        this.fingerprintCalculator = fingerprintCalculator;
        this.isNew = isNew;
        this.originalFingerprint = fingerprintCalculator.calculate(entity);
    }

    @Override
    public TrackableEntity getTrackedEntity() {
        return entity;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public boolean hasChanged() {
        if (isNew) {
            return true;
        }
        return originalFingerprint != fingerprintCalculator.calculate(entity);
    }
}
