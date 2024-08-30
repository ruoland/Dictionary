package org.ruoland.dictionary.dictionary.dictionary.entity;

import net.minecraft.world.entity.EntityType;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseContent;

import java.util.Optional;

public class EntityContent extends BaseContent {
    private transient EntityType entityType;

    public EntityContent(EntityType entityType) {
        super(entityType.getDescriptionId());
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        if(entityType == null) {
            Optional<EntityType<?>> type =EntityType.byString(getContentId());
            Dictionary.LOGGER.info("엔티티 타입이 없어 가져옵니다. {}, {}", getContentId(), type);
            entityType = type.get();
        }
        return entityType;
    }


}