package org.ruoland.dictionary.dictionary.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.dictionary.dictionary.ItemManager;
import org.ruoland.dictionary.dictionary.dictionary.TagManager;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemGroupContent;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.SubData;

import java.util.ArrayList;

public class SubItemButton extends Button {
    private final ArrayList<ItemStack> itemList = new ArrayList<>();
    private ItemStack itemStack;
    int itemIndex;
    int itemIndexTick;
    protected SubItemButton(ItemStack itemStack, int x, int y, int witdh, int height, Component component, OnPress onPress, CreateNarration createNarration) {
        super(x, y, witdh, height, component, onPress, DEFAULT_NARRATION);
        this.itemStack = itemStack;


    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderWidget(guiGraphics, i, j, f);

        if(itemList.size() <= itemIndex)
        {
            itemIndex = 0;
        }


        itemIndexTick++;
        if(itemList.size() <= itemIndex)
        {
            itemIndex = 0;
        }
        else if((itemIndexTick)% 30 == 0)
        {
            itemIndex++;
        }
        if(!itemList.isEmpty() && itemList.size() < itemIndex)
            guiGraphics.renderItem(itemList.get(itemIndex), getX() - 20, getY());
        else
            guiGraphics.renderItem(itemStack, getX() - 20, getY());

    }

    public SubItemButton addGroupItems(){
        SubData subData = TagManager.getTagManager().getItemTag(itemStack).getSubData();
        itemList.add(itemStack);
        for(ItemGroupContent groupContent : subData.getGroupMap().values()){
            for(ItemContent content :groupContent.getContentMap().values())
                itemList.add(ItemManager.getItemStackMap().get(content.getItemID()));
        }
        return this;
    }

}
