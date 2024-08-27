package org.ruoland.dictionary.dictionary.dictionary.itemcontent;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.ItemManager;
import org.ruoland.dictionary.dictionary.dictionary.LangManager;
import org.ruoland.dictionary.dictionary.dictionary.TagManager;
import org.ruoland.dictionary.dictionary.dictionary.VariableManager;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.IContent;

public class ItemContent implements IContent {
    private transient ItemStack itemStack; //대상 아이템
    @SerializedName("아이템 아이디")
    String itemID = "";
    @SerializedName("아이템 영어 이름")
    String englishName;

    @SerializedName("아이템 설명")
    private String dictionary;


    ItemContent(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemID = itemStack.getDescriptionId();
        englishName = LangManager.getEnglishName(itemStack);
        dictionary = DefaultDictionary.ITEM_DESC; // 기본값으로 초기화
        Dictionary.LOGGER.info("ItemContent created for {}: description = {}", itemID, dictionary);
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getItemID() {
        return itemID;
    }

    public String getDictionary(boolean isDebug) {
        Dictionary.LOGGER.info("getDictionary called for {}: current description = {}, debug mode: {}", itemID, dictionary, isDebug);
        if(!isDebug && dictionary != null)
            dictionary = VariableManager.replaceVariable(itemStack, dictionary);
        Dictionary.LOGGER.info("getDictionary after replace for {}: new description = {}", itemID, dictionary);
        return dictionary;
    }
    public void setDictionary(String newDictionary) {
        Dictionary.LOGGER.info("setDictionary called for {}: old = {}, new = {}", itemID, dictionary, newDictionary);
        this.dictionary = newDictionary;
    }
    @Override
    public boolean isGroup() {
        return false;
    }

    public ItemStack getItemStack() {
        System.out.println(ItemManager.getItemStackMap().get(itemID));
        return ItemManager.getItemStackMap().get(itemID);
    }
}
