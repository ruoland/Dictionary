package org.ruoland.dictionary.dictionary.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.item.EnumItemTag;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemSubData;
import org.ruoland.dictionary.dictionary.dictionary.manager.TagManager;

import java.util.LinkedList;

public class ItemsTagScreen extends DebugScreen{
    private final LinkedList<ItemSubData> itemTags = new LinkedList<>();
    public ItemsTagScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        itemTags.clear();
        for(EnumItemTag tag : EnumItemTag.values())
            itemTags.add(TagManager.getTagManager().getItemTag(tag).getSubData());
        int Y = 0;
        int xLine = 0;
        for(ItemSubData tag : itemTags) {
            ItemStack itemStack = tag.getFirstItem();
            if(itemStack == null) {
                Dictionary.LOGGER.info(tag.getTag() +"에는 대표 아이템이 없음.");
                continue;
            }

            String displayName = itemStack.getDisplayName().getString();
            Dictionary.LOGGER.warn("대표 아이템 발견: {}, 서브 데이터 객체: {}, 태그: {}", displayName, tag, tag.getTag());
            addRenderableWidget(new SubItemButton(itemStack, guiLeft + 30 + xLine, guiTop + Y, 50, 20, Component.literal(tag.getTag()), button -> {
                ItemSubData itemSubData = TagManager.getTagManager().getItemTag(itemStack).getSubData();
                minecraft.setScreen(new SubDataScreen(minecraft.screen, itemSubData));

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
