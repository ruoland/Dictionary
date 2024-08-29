package org.ruoland.dictionary.dictionary.dictionary.entity;

import net.minecraft.world.entity.LivingEntity;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseContent;

public class EntityContent extends BaseContent {
    private transient LivingEntity livingEntity;

    public EntityContent(LivingEntity livingEntity) {
        super(livingEntity.getType().getDescriptionId());
        this.livingEntity = livingEntity;
    }

    public void setLivingEntity(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    @Override
    public String getDictionary() {
        return super.getDictionary();
    }

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }

}