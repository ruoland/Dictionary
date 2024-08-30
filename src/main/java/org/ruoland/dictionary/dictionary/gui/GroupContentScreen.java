package org.ruoland.dictionary.dictionary.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseContent;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseGroupContent;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.IDictionaryAdapter;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemContent;

public class GroupContentScreen extends DebugScreen{


    BaseGroupContent<IDictionaryAdapter, BaseContent> content = null;
    protected GroupContentScreen(Screen screen, BaseGroupContent content) {
        super(Component.literal("그룹 아이템"));
        this.content = content;

    }
    @Override
    protected void init() {
        super.init();
        int Y = 0;
        int xLine = 0;
        for(String tag : content.getContentMap().keySet()) {
            BaseContent baseContent = content.getContentMap().get(tag);
            if(baseContent instanceof ItemContent itemContent ) {
                ItemStack itemStack = itemContent.getItemStack();
                addRenderableWidget(new SubItemButton(itemStack, guiLeft + 30 + xLine, guiTop + Y, 50, 20, Component.literal(itemStack.getDisplayName().getString()), new Button.OnPress() {
                    @Override
                    public void onPress(Button button) {
                        ContentScreen contentScreen = new ContentScreen(minecraft.screen, itemStack);
                        minecraft.setScreen(contentScreen);
                    }
                }, null));
                Y += 30;
                if (Y >= height - 40) {
                    Y = 0;
                    xLine += 80;

                }

            }
        }
    }
}
