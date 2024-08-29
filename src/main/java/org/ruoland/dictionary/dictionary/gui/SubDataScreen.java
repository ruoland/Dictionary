package org.ruoland.dictionary.dictionary.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemGroupContent;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemSubData;

public class SubDataScreen extends DebugScreen{
    ItemSubData itemSubData;

    protected SubDataScreen(Screen screen, ItemSubData content) {
        super(Component.literal("그룹 아이템"));
        this.itemSubData = content;
        this.lastScreen = screen;
    }

    @Override
    protected void init() {
        super.init();
        int Y = 0;
        int xLine = 0;
        for(String tag : itemSubData.getGroupMap().keySet()) {
            ItemGroupContent groupContent = itemSubData.getGroupMap().get(tag);
            ItemStack itemStack = groupContent.getZeroItem();
            addRenderableWidget(new SubItemButton(itemStack, guiLeft + 30 + xLine, guiTop + Y, 50, 20, (Component.literal(groupContent.getGroupName())), new Button.OnPress() {
                @Override
                public void onPress(Button button) {

                    GroupContentScreen contentScreen = new GroupContentScreen(minecraft.screen, groupContent);
                    minecraft.setScreen(contentScreen);
                }
                }, null));
            Y += 30;
            if (Y >= height - 40) {
                Y = 0; //60으로
                xLine += 80;
            }
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
