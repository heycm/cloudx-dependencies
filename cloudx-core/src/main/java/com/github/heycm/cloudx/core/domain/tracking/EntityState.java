package com.github.heycm.cloudx.core.domain.tracking;

/**
 * 实体状态接口，提供实体变更的状态检测
 * @author heycm
 * @version 1.0
 * @since 2025/11/16 13:45
 */
public interface EntityState {

    /**
     * 获取追踪的实体
     * @return 被追踪的实体
     */
    TrackableEntity getTrackedEntity();

    /**
     * 是否是新增的实体
     * @return 是否是新增的实体
     */
    boolean isNew();

    /**
     * 是否有变更
     * @return 是否有变更
     */
    boolean hasChanged();
}
