package com.github.heycm.cloudx.core.domain.tracking;

import com.github.heycm.cloudx.core.domain.model.Identifier;
import java.util.Objects;

/**
 * 被删除实体的占位符
 * @author heycm
 * @version 1.0
 * @since 2025/11/16 15:02
 */
public class RemovedEntityPlaceholder implements TrackableEntity {

    private final Identifier entityId;

    public RemovedEntityPlaceholder(Identifier entityId) {
        this.entityId = entityId;
    }

    @Override
    public Identifier getEntityId() {
        return entityId;
    }

    @Override
    public String getEntityType() {
        return "RemovedEntity";
    }

    @Override
    public String toString() {
        return "RemovedEntity{id=" + entityId + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RemovedEntityPlaceholder other = (RemovedEntityPlaceholder) obj;
        return Objects.equals(entityId, other.entityId);
    }
}
