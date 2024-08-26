package org.ruoland.dictionary.dictionary.gui.dev;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.ruoland.dictionary.dictionary.gui.DebugScreen;

public class EntityScreen extends DebugScreen {

    protected EntityScreen(Component pTitle, Entity entity) {
        super(pTitle);
        entity.getType().getDescriptionId();
    }


}
