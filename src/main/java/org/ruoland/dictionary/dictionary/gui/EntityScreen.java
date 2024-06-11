package org.ruoland.dictionary.dictionary.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class EntityScreen extends DebugScreen{

    protected EntityScreen(Component pTitle, Entity entity) {
        super(pTitle);
        entity.getType().getDescriptionId();
    }


}
