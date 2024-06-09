package org.ruoland.dictionary.dictionary.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class DictionaryContainer extends ContainerScreen implements Container {
    private ArrayList<ItemStack> itemStackList = new ArrayList<>();

    public DictionaryContainer(ChestMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(ChestMenu.sixRows(0, pPlayerInventory), pPlayerInventory, pTitle);

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public int getContainerSize() {
        return 32;
    }

    @Override
    public boolean isEmpty() {
        return itemStackList.isEmpty();
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return itemStackList.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return itemStackList.get(pSlot);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return null;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        this.itemStackList.set(pSlot, pStack);
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void clearContent() {

    }
}
