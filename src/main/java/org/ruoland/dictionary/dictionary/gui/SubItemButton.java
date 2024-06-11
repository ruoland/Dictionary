package org.ruoland.dictionary.dictionary.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class SubItemButton extends Button {
    private ItemStack itemStack;
    protected SubItemButton(ItemStack itemStack, int x, int y, int witdh, int height, Component component, OnPress onPress, CreateNarration createNarration) {
        super(x, y, witdh, height, component, onPress, DEFAULT_NARRATION);
        this.itemStack = itemStack;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderWidget(guiGraphics, i, j, f);
        guiGraphics.renderItem(itemStack, getX() - 20, getY());
    }
}
