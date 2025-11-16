package com.github.heycm.cloudx.core.domain.tracking;

import java.util.Objects;

/**
 * 实体状态指纹计算器实现
 * @author heycm
 * @version 1.0
 * @since 2025/11/16 14:28
 */
public class StateFingerprintCalculator implements FingerprintCalculator {

    @Override
    public int calculate(TrackableEntity entity) {
        // 实体自实现的指纹计算逻辑
        if (entity instanceof Fingerprintable) {
            return ((Fingerprintable) entity).getFingerprint();
        }
        return calculateDefaultFingerprint(entity);
    }

    private int calculateDefaultFingerprint(TrackableEntity entity) {
        return Objects.hash(entity.getEntityId(), entity.getEntityType(), getFingerprintFieldsHash(entity));
    }

    private int getFingerprintFieldsHash(TrackableEntity entity) {
        return Objects.hash(getFingerprintFields(entity));
    }

    private Object[] getFingerprintFields(TrackableEntity entity) {
        if (entity instanceof FingerprintFieldProvider) {
            return ((FingerprintFieldProvider) entity).getFingerprintFields();
        }
        throw new UnsupportedOperationException("FingerprintFieldProvider not supported by entity: " + entity.getClass().getName());
    }
}
