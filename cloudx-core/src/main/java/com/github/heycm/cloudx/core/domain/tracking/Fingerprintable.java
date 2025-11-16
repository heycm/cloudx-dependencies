package com.github.heycm.cloudx.core.domain.tracking;

/**
 * 实体指纹接口，用于计算实体的指纹，提供自定义的指纹计算逻辑，提供更精确或更高效的变更检测
 * @author heycm
 * @version 1.0
 * @since 2025/11/15 14:29
 */
public interface Fingerprintable {

    /**
     * 获取实体的指纹
     * @return 实体的指纹
     */
    int getFingerprint();
}
