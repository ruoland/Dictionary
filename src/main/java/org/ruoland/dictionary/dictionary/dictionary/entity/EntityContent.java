package org.ruoland.dictionary.dictionary.dictionary.entity;

import net.minecraft.world.entity.EntityType;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.DictionaryContent;

public class EntityContent extends DictionaryContent {
    private final EntityType<?> entityType;

    public EntityContent(String id, EntityType<?> entityType) {
        super(id);
        this.entityType = entityType;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

}