package org.ruoland.dictionary.dictionary.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.dictionary.dictionary.TagManager;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.EnumTag;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemsTag;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.SubData;

import java.util.ArrayList;

public class SubDataScreen extends DebugScreen{
    private ArrayList<SubData> itemTags = new ArrayList<>();
    public SubDataScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        for(EnumTag tag : EnumTag.values())
            itemTags.add(TagManager.getTagManager().getItemTag(tag).getSubData());
        int Y = 100;
        for(SubData tag : itemTags) {
            ItemStack itemStack = tag.getZeroItem();
            if(itemStack == null) {
                System.out.println(itemStack + " - " + tag.tag.name());
                continue;

            }

            String displayName = itemStack.getDisplayName().getString();

            addRenderableWidget(new SubItemButton(itemStack, guiLeft, guiTop - 100 + Y, font.width(displayName), 20, Component.literal(tag.tag.name()), new Button.OnPress() {
                @Override
                public void onPress(Button button) {
                    System.out.println(displayName);
                }
            }, null));
            Y += 20;
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

    }
}
