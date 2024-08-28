package org.ruoland.dictionary.dictionary.dictionary.manager;


import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.item.DefaultDictionary;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.item.ItemGroupContent;
import org.ruoland.dictionary.dictionary.dictionary.item.SubData;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemManager {

    public static ArrayList<ItemStack> getItemList() {
        return MinecraftDataManager.getItemList();
    }

    public static HashMap<String, ItemStack> getItemStackMap() {
        return getItemStackMap();
    }

    public static String getContent(ItemStack itemStack) {
        Dictionary.LOGGER.info("getContent called for item: {}", itemStack.getDescriptionId());

        TagManager tagManager = TagManager.getTagManager();
        SubData sub = tagManager.getItemTag(itemStack).getSubData();
        ItemGroupContent itemGroup = sub.getItemGroup(itemStack);
        ItemContent content = itemGroup.getItemContent(itemStack);

        //Dictionary.LOGGER.info("Content retrieved - itemId: {}, description: {}", itemStack.getDescriptionId(), content.getDictionary(false));

        StringBuilder stringBuffer = new StringBuilder();
        if(sub.getSubDictionary() == null && content.getDictionary(false) == null) {
            stringBuffer.append(sub.getItemGroup(itemStack).getDictionary());
        } else {

            stringBuffer.append("아이템 설명:\n");
            stringBuffer.append(itemGroup.getDictionary()).append("\n\n");

            String itemDescription = content.getDictionary(false);

            //Dictionary.LOGGER.info("Final item description for {}: {}", itemStack.getDescriptionId(), itemDescription);

            if(itemDescription != null && !itemDescription.equals(DefaultDictionary.ITEM_DESC)) {
                return itemDescription;
            }

        }


        return stringBuffer.toString();
    }

}
