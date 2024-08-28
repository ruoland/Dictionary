package org.ruoland.dictionary.dictionary.dictionary.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class CubeEntity extends Mob {
    public CubeEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }
}
