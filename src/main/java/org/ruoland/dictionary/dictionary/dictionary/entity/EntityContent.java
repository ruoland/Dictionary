package org.ruoland.dictionary.dictionary.dictionary.entity;

import net.minecraft.world.entity.EntityType;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseContent;

public class EntityContent extends BaseContent {
    private transient EntityType<?> entityType;

    public EntityContent(EntityType<?> entityType) {
        super(entityType.getDescriptionId());
        this.entityType = entityType;
    }

    public EntityType<?> getEntityType() {
        if(entityType == null)
            entityType = EntityType.byString(getID()).get();
        return entityType;
    }

}