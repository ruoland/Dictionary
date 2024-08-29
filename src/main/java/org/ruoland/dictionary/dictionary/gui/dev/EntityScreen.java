package org.ruoland.dictionary.dictionary.gui.dev;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.ruoland.dictionary.dictionary.gui.DebugScreen;

public class EntityScreen extends DebugScreen {
    private Entity entity;
    public EntityScreen(Component pTitle, Entity entity) {
        super(pTitle);
        this.entity = entity;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);


    }
}
