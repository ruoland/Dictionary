package org.ruoland.dictionary.dictionary.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemGroupContent;

public class GroupContentScreen extends DebugScreen{


    ItemGroupContent content = null;
    protected GroupContentScreen(Screen screen, ItemGroupContent content) {
        super(Component.literal("그룹 아이템"));
        this.content = content;

    }
    @Override
    protected void init() {
        super.init();
        int Y = 0;
        int xLine = 0;
        for(String tag : content.getContentMap().keySet()) {
            ItemContent itemContent = content.getContentMap().get(tag);
                ItemStack itemStack = itemContent.getItemStack();
                addRenderableWidget(new SubItemButton(itemStack, guiLeft + 30 + xLine, guiTop + Y, 50, 20, Component.literal(itemStack.getDisplayName().getString()), new Button.OnPress() {
                    @Override
                    public void onPress(Button button) {
                        ContentScreen contentScreen = new ContentScreen(minecraft.screen, itemStack, false);
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
}
