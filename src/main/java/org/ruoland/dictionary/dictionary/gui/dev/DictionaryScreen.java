package org.ruoland.dictionary.dictionary.gui.dev;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.dictionary.dictionary.manager.DataManager;
import org.ruoland.dictionary.dictionary.gui.DebugScreen;

import java.util.ArrayList;

public class DictionaryScreen extends DebugScreen {
    private ArrayList<ItemStack> itemStackList = new ArrayList<>();
    protected DictionaryScreen(Component pTitle) {
        super(pTitle);
        itemStackList.addAll(DataManager.getItemStackMap().values());
    }

    @Override
    protected void init() {
        super.init();

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        int x= 0, line=0, id = 0;
        for(int i = 0; i < itemStackList.size();i++){
            x+= 20;

            renderItem(pGuiGraphics, x, line * 20, 1, itemStackList.get(i), pPartialTick);
            if(i % 10 == 0){
                line++;
            }
        }
    }

    public void renderItem(GuiGraphics pGuiGraphics, int resultX, int resultY, float scale, ItemStack item, float pPartialTick){
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.setColor(123,123,1, 0.5F);
        pGuiGraphics.pose().scale(scale,scale,scale);
        pGuiGraphics.renderItem(item, resultX, resultY);
        pGuiGraphics.pose().popPose();
    }
}
