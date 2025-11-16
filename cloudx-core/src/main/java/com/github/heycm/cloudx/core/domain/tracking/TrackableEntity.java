package com.github.heycm.cloudx.core.domain.tracking;

import com.github.heycm.cloudx.core.domain.model.Identifier;

/**
 * 可追踪实体接口
 * @author heycm
 * @version 1.0
 * @since 2025/11/15 14:21
 */
public interface TrackableEntity {

    /**
     * 获取实体ID
     * @return 实体ID，唯一标识，通常为数据库主键
     */
    Identifier getEntityId();

    /**
     * 获取实体类型
     * @return 实体类型标识，区分不同类型的实体
     */
    String getEntityType();
}
