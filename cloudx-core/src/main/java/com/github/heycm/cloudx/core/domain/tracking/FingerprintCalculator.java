package com.github.heycm.cloudx.core.domain.tracking;

/**
 * 指纹计算器
 * @author heycm
 * @version 1.0
 * @since 2025/11/15 14:26
 */
public interface FingerprintCalculator {

    /**
     * 计算实体指纹
     * @param entity 可追踪实体
     * @return 指纹
     */
    int calculate(TrackableEntity entity);
}
