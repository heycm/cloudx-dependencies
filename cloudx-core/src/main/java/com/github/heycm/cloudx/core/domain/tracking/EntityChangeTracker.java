package com.github.heycm.cloudx.core.domain.tracking;

import com.github.heycm.cloudx.core.domain.model.Identifier;
import java.util.Set;

/**
 * 实体变更追踪器接口，变更追踪体系的核心接口，负责管理实体状态追踪和变更检测
 * @author heycm
 * @version 1.0
 * @since 2025/11/16 13:28
 */
public interface EntityChangeTracker<T extends TrackableEntity> {

    /**
     * 开始追踪实体变更
     * @param entity 被追踪的实体对象
     */
    void startTracking(T entity);

    /**
     * 停止追踪实体变更
     * @param entity 被追踪的实体对象
     */
    void stopTracking(T entity);

    /**
     * 显示标记实体为已删除，表示永久删除（数据库删除），区别于 stopTracking 在追踪体系中临时移除
     * @param entity 被标记的实体对象
     */
    void markAsRemoved(T entity);

    /**
     * 检测实体变更，返回实体变更结果集
     * @return 实体变更结果集
     */
    ChangeSet detectChanges();

    /**
     * 清空实体变更结果集，通常在变更处理完成后调用，准备下一次变更检测开始之前
     */
    void clear();

    /**
     * 设置初始追踪的实体ID集合，通常在启动追踪体系时调用，作为删除检查的基准参照集合
     * @param initialIds 初始追踪的实体ID集合
     */
    void setInitialTrackedIds(Set<Identifier> initialIds);
}
