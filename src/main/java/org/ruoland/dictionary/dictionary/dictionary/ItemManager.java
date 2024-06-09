package org.ruoland.dictionary.dictionary.dictionary;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
        SubData sub = TagManager.getTagManager().getItemTag(itemStack).getSubData();
        ItemGroupContent itemGroup = sub.getItemGroup(itemStack);
        ItemContent content = itemGroup.getItemContent(itemStack);
        StringBuffer stringBuffer = new StringBuffer();
        if(sub.isReplace())//태그 설명으로 대체하기.
            stringBuffer.append(sub.getItemGroup(itemStack).getDictionary());
        else {
            stringBuffer.append(sub.getSubDictionary()).append("\n");
            stringBuffer.append(itemGroup.getDictionary()).append("\n");
        }
        if(content.getDictionary().equals("설명")) //정확한 아이템의 설명이 없는 경우 대신 태그 설명을 반환함
            return stringBuffer.toString();
        return content.getDictionary();
    }

}
