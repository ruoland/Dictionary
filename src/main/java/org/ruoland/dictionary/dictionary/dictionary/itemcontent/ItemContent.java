package org.ruoland.dictionary.dictionary.dictionary.itemcontent;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.dictionary.dictionary.LangManager;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.IContent;

public class ItemContent implements IContent {
    private transient ItemStack itemStack; //대상 아이템
    @SerializedName("아이템 아이디")
    String itemID = "";
    @SerializedName("아이템 영어 이름")
    String englishName;

    @SerializedName("아이템 설명")
    String dictionary = DefaultDictionary.ITEM_DESC;

    ItemContent(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemID = itemStack.getDescriptionId();
        englishName = LangManager.getEnglishName(itemStack);
    }

    public String getItemID() {
        return itemID;
    }

    public String getDictionary() {
        return dictionary;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
