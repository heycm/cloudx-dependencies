package com.github.heycm.cloudx.core.domain.tracking;

import com.github.heycm.cloudx.core.domain.model.Identifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于状态快照的变更追踪器实现
 * @author heycm
 * @version 1.0
 * @since 2025/11/16 14:38
 */
public class StateSnapshotChangeTracker<T extends TrackableEntity> implements EntityChangeTracker<T> {

    /**
     * 实体状态快照：实体id -> 实体状态
     */
    private final Map<Identifier, EntityState> stateSnapshots = new ConcurrentHashMap<>();

    /**
     * 初始追踪的实体ID集合
     */
    private final Set<Identifier> originallyTrackedIds = new HashSet<>();

    /**
     * 删除的实体ID集合
     */
    private final Set<Identifier> removedEntityIds = new HashSet<>();

    /**
     * 实体状态工厂，用于拍摄状态快照
     */
    private final EntityStateFactory entityStateFactory;

    public StateSnapshotChangeTracker(EntityStateFactory entityStateFactory) {
        this.entityStateFactory = entityStateFactory;
    }

    @Override
    public void startTracking(T entity) {
        Identifier entityId = entity.getEntityId();

        // 未被追踪的实体，创建状态快照
        if (!stateSnapshots.containsKey(entityId)) {
            stateSnapshots.put(entityId, entityStateFactory.create(entity));
            originallyTrackedIds.add(entityId);
        }

        // 若之前标记为删除的，则取消标记
        removedEntityIds.remove(entityId);
    }

    @Override
    public void stopTracking(T entity) {
        Identifier entityId = entity.getEntityId();

        // 从状态快照中移除
        stateSnapshots.remove(entityId);

        // 关键：只有原本就被追踪的实体，才被认为有删除
        if (originallyTrackedIds.contains(entityId)) {
            removedEntityIds.add(entityId);
            originallyTrackedIds.remove(entityId);
        }
    }

    @Override
    public void markAsRemoved(T entity) {
        Identifier entityId = entity.getEntityId();
        // 从状态快照中移除
        stateSnapshots.remove(entityId);
        // 标记为删除
        removedEntityIds.add(entityId);
        // 从初始集合中移除
        originallyTrackedIds.remove(entityId);
    }

    @Override
    public ChangeSet detectChanges() {
        ChangeSet changeSet = new ChangeSet();
        // 检测新增和更新的实体
        detectNewAndUpdatedEntities(changeSet);
        // 检测删除的实体
        detectRemovedEntities(changeSet);
        return changeSet;
    }

    @Override
    public void clear() {
        stateSnapshots.clear();
        removedEntityIds.clear();
        originallyTrackedIds.clear();
    }

    @Override
    public void setInitialTrackedIds(Set<Identifier> initialIds) {
        originallyTrackedIds.clear();
        originallyTrackedIds.addAll(initialIds);
    }

    /**
     * 检测新增和更新的实体 <br/>
     * 检测逻辑：遍历所有状态快照，根据快照状态识别新增和更新的实体，添加到变更结果集中
     * @param changeSet 变更结果集
     */
    private void detectNewAndUpdatedEntities(ChangeSet changeSet) {
        for (EntityState snapshot : stateSnapshots.values()) {
            TrackableEntity trackedEntity = snapshot.getTrackedEntity();
            // 新增检测
            if (snapshot.isNew()) {
                changeSet.addNewEntity(trackedEntity);
            }
            // 更新检测
            else if (snapshot.hasChanged()) {
                changeSet.addUpdatedEntity(trackedEntity);
            }
        }
    }

    /**
     * 检测删除的实体 <br/>
     * 检测逻辑：遍历所有删除的实体ID，根据ID创建删除占位符，添加到变更结果集中
     * @param changeSet 变更结果集
     */
    private void detectRemovedEntities(ChangeSet changeSet) {
        for (Identifier entityId : removedEntityIds) {
            changeSet.addRemovedEntity(new RemovedEntityPlaceholder(entityId));
        }
    }
}
