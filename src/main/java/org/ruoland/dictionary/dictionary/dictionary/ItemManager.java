package org.ruoland.dictionary.dictionary.dictionary;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.DefaultDictionary;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemContent;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.ItemGroupContent;
import org.ruoland.dictionary.dictionary.dictionary.itemcontent.SubData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemManager {

    private static final ArrayList<ItemStack> ITEM_LIST = new ArrayList<>();
    private static final HashMap<String, ItemStack> ITEM_STACK_MAP = new HashMap<>();

    /**
     *마인크래프트 기본 아이템 전부 불러오기
     * 리플렉션 제외하면 모든 아이템을 가져올 수 없는 것 같음
     */
    public static ItemStack[] loadMinecraftItems() throws RuntimeException, IllegalAccessException {
        Field[] itemFields = Items.class.getFields();
        ItemStack[] itemStacks = new ItemStack[itemFields.length];
        for (int i = 0; i < itemFields.length; i++) {
            if (itemFields[i].getType() == Item.class) {
                ItemStack itemStack = new ItemStack((Item) itemFields[i].get(null));
                ITEM_STACK_MAP.put(itemStack.getDescriptionId(), itemStack);
                ITEM_LIST.add(itemStack);
            }
        }
        return itemStacks;
    }

    public static ArrayList<ItemStack> getItemList() {
        return ITEM_LIST;
    }

    public static HashMap<String, ItemStack> getItemStackMap() {
        return ITEM_STACK_MAP;
    }
    public static String getContent(ItemStack itemStack) {
        Dictionary.LOGGER.info("getContent called for item: {}", itemStack.getDescriptionId());

        TagManager tagManager = TagManager.getTagManager();
        SubData sub = tagManager.getItemTag(itemStack).getSubData();
        ItemGroupContent itemGroup = sub.getItemGroup(itemStack);
        ItemContent content = itemGroup.getItemContent(itemStack);

        //Dictionary.LOGGER.info("Content retrieved - itemId: {}, description: {}", itemStack.getDescriptionId(), content.getDictionary(false));

        StringBuilder stringBuffer = new StringBuilder();
        if(sub.isReplace() || sub.getSubDictionary() == null || content.getDictionary(false) == null) {
            stringBuffer.append(sub.getItemGroup(itemStack).getDictionary());
        } else {
            stringBuffer.append(sub.getSubDictionary()).append("\n");
            stringBuffer.append(itemGroup.getDictionary()).append("\n");
            stringBuffer.append(sub.getItemGroup(itemStack).getDictionary()).append("\n");

            String itemDescription = content.getDictionary(false);

            //Dictionary.LOGGER.info("Final item description for {}: {}", itemStack.getDescriptionId(), itemDescription);

            if(!itemDescription.equals(DefaultDictionary.ITEM_DESC)) {
                return itemDescription;
            }

        }


        return stringBuffer.toString();
    }

}
