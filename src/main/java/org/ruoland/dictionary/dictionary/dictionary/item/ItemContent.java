package org.ruoland.dictionary.dictionary.dictionary.item;

import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseContent;
import org.ruoland.dictionary.dictionary.dictionary.manager.DataManager;

public class ItemContent extends BaseContent {
    private transient ItemStack itemStack; //대상 아이템

    public ItemContent(ItemStack itemStack) {
        super(itemStack.getDescriptionId());
        this.itemStack = itemStack;

    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getDictionary() {
        if (dictionary == null ) {
            return null;
        }
        return dictionary;
    }

    public ItemStack getItemStack() {
        return DataManager.getItemStackMap().get(getID());
    }
}
