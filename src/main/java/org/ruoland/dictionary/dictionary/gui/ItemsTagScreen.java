package org.ruoland.dictionary.dictionary.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.item.EnumTag;
import org.ruoland.dictionary.dictionary.dictionary.item.SubData;
import org.ruoland.dictionary.dictionary.dictionary.manager.TagManager;

import java.util.LinkedList;

public class ItemsTagScreen extends DebugScreen{
    private LinkedList<SubData> itemTags = new LinkedList<>();
    public ItemsTagScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        itemTags.clear();
        for(EnumTag tag : EnumTag.values())
            itemTags.add(TagManager.getTagManager().getItemTag(tag).getSubData());
        int Y = 0;
        int xLine = 0;
        for(SubData tag : itemTags) {
            ItemStack itemStack = tag.getZeroItem();
            if(itemStack == null) {
                Dictionary.LOGGER.info(tag.tag+"에는 대표 아이템이 없음.");
                continue;
            }

            String displayName = itemStack.getDisplayName().getString();
            addRenderableWidget(new SubItemButton(itemStack, guiLeft + 30 + xLine, guiTop + Y, 50, 20, Component.literal(tag.tag.name()), new Button.OnPress() {
                @Override
                public void onPress(Button button) {
                    SubData subData = TagManager.getTagManager().getItemTag(itemStack).getSubData();
                    minecraft.setScreen(new SubDataScreen(minecraft.screen, subData));

                }
            }, null));
            Y += 30;
            if( Y >= height - 40){
                Y = 0; //60으로
                xLine +=80;
            }

            if(xLine >= width){
                xLine = 0;
            }
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
