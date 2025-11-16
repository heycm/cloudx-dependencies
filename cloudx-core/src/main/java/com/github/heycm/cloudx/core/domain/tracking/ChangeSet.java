package com.github.heycm.cloudx.core.domain.tracking;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 实体变更结果集，用于保存实体的变更信息
 * @author heycm
 * @version 1.0
 * @since 2025/11/16 13:29
 */
public class ChangeSet {

    /**
     * 新增的实体集合
     */
    private final Set<TrackableEntity> newEntities = new LinkedHashSet<>();

    /**
     * 更新的实体集合
     */
    private final Set<TrackableEntity> updatedEntities = new LinkedHashSet<>();

    /**
     * 删除的实体集合
     */
    private final Set<TrackableEntity> removedEntities = new LinkedHashSet<>();

    /**
     * 添加新增的实体
     * @param entity
     */
    public void addNewEntity(TrackableEntity entity) {
        newEntities.add(entity);
    }

    /**
     * 添加更新的实体
     * @param entity
     */
    public void addUpdatedEntity(TrackableEntity entity) {
        updatedEntities.add(entity);
    }

    /**
     * 添加删除的实体
     * @param entity
     */
    public void addRemovedEntity(TrackableEntity entity) {
        removedEntities.add(entity);
    }

    /**
     * 获取新增的实体集合
     * @return 新增的实体集合，不可变视图
     */
    public Set<TrackableEntity> getNewEntities() {
        return new HashSet<>(newEntities);
    }

    /**
     * 获取更新的实体集合
     * @return 更新的实体集合，不可变视图
     */
    public Set<TrackableEntity> getUpdatedEntities() {
        return new HashSet<>(updatedEntities);
    }

    /**
     * 获取删除的实体集合
     * @return 删除的实体集合，不可变视图
     */
    public Set<TrackableEntity> getRemovedEntities() {
        return new HashSet<>(removedEntities);
    }

    /**
     * 是否有变更
     * @return 是否有变更
     */
    public boolean hasChanges() {
        return !newEntities.isEmpty() || !updatedEntities.isEmpty() || !removedEntities.isEmpty();
    }

    /**
     * 获取变更数量
     * @return 变更总数
     */
    public int getChangeCount() {
        return newEntities.size() + updatedEntities.size() + removedEntities.size();
    }

    /**
     * 清空变更结果，通常在变更处理完成后调用，避免重复处理相同的变更
     */
    public void clear() {
        newEntities.clear();
        updatedEntities.clear();
        removedEntities.clear();
    }

    @Override
    public String toString() {
        return String.format("ChangeSet{new=%d, updated=%d, removed=%d}", newEntities.size(), updatedEntities.size(), removedEntities.size());
    }
}
