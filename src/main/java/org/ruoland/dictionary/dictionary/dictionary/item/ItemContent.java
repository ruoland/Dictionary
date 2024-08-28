package org.ruoland.dictionary.dictionary.dictionary.item;

import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.DictionaryContent;
import org.ruoland.dictionary.dictionary.dictionary.manager.ContentManager;
import org.ruoland.dictionary.dictionary.dictionary.manager.VariableManager;

public class ItemContent extends DictionaryContent {
    private transient ItemStack itemStack; //대상 아이템



    public ItemContent(ItemStack itemStack) {
        super(itemStack.getDescriptionId());
        this.itemStack = itemStack;
        setDictionary(DefaultDictionary.ITEM_DESC); // 기본값으로 초기화
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getDictionary() {
        Dictionary.LOGGER.info("getDictionary called for {}: current description = {}", getID(), dictionary);
        setDictionary(VariableManager.replaceVariable(itemStack, dictionary));
        Dictionary.LOGGER.info("getDictionary after replace for {}: new description = {}", getID(), dictionary);
        return dictionary;
    }

    public ItemStack getItemStack() {
        return ContentManager.getItemStackMap().get(getID());
    }
}
