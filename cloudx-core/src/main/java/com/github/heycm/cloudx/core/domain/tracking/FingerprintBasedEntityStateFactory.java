package com.github.heycm.cloudx.core.domain.tracking;

import com.github.heycm.cloudx.core.domain.model.Identifier;

/**
 * 基于实体指纹的状态创建工厂实现
 * @author heycm
 * @version 1.0
 * @since 2025/11/16 14:48
 */
public class FingerprintBasedEntityStateFactory implements EntityStateFactory {

    /**
     * 指定使用基于指纹的状态计算器，而不是使用接口
     */
    private final StateFingerprintCalculator fingerprintCalculator;

    public FingerprintBasedEntityStateFactory(StateFingerprintCalculator fingerprintCalculator) {
        this.fingerprintCalculator = fingerprintCalculator;
    }

    @Override
    public EntityState create(TrackableEntity entity) {
        boolean isNew = determineIfNew(entity);
        return new FingerprintBasedEntityState(entity, fingerprintCalculator, isNew);
    }

    private boolean determineIfNew(TrackableEntity entity) {
        Identifier entityId = entity.getEntityId();
        return entityId == null;
    }
}
