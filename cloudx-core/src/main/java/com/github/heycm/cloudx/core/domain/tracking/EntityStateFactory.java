package com.github.heycm.cloudx.core.domain.tracking;

/**
 * 实体状态创建工厂接口
 * @author heycm
 * @version 1.0
 * @since 2025/11/16 14:47
 */
public interface EntityStateFactory {

    /**
     * 创建实体状态
     * @param entity 实体对象
     * @return 实体状态
     */
    EntityState create(TrackableEntity entity);
}
