package org.ruoland.dictionary.dictionary.dictionary.itemcontent;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;
import org.ruoland.dictionary.Dictionary;

import java.util.HashMap;

public class ItemGroupContent {

    @SerializedName("그룹 대표 이름")
    String groupName = DefaultDictionary.GROUP_NAME;
    
    @SerializedName("그룹 설명")
    String dictionary = DefaultDictionary.GROUP_DESC;

    @SerializedName("아이템들")
    private HashMap<String, ItemContent> itemContentMap = new HashMap<>();

    public ItemGroupContent(){

    }

    public ItemStack getZeroItem(){
        for(ItemContent content : itemContentMap.values()){

            return content.getItemStack();
        }
        return null;
    }
    public HashMap<String, ItemContent> getContentMap() {
        return itemContentMap;
    }

    public void clear(){
        itemContentMap.clear();;
    }
    public String getDictionary() {
        return dictionary;
    }

    public void add(ItemStack itemStack) {
        String itemId = itemStack.getDescriptionId();
        if (itemContentMap.containsKey(itemId)) {
            Dictionary.LOGGER.trace("Item already exists in group: {}", itemId);
            return;
        }

        ItemContent newContent = new ItemContent(itemStack);
        if (itemContentMap.containsKey(itemId)) {
            String existingDescription = itemContentMap.get(itemId).getDictionary(true);
            newContent.setDictionary(existingDescription);
            Dictionary.LOGGER.info("Copying existing description for item: {}", itemId);
        }

        itemContentMap.put(itemId, newContent);
        Dictionary.LOGGER.info("Added new item to group: {}, description: {}", itemId, newContent.getDictionary(true));
    }

    public void addAll(ItemGroupContent groupContent){
        this.itemContentMap.putAll(groupContent.getContentMap());
    }
    public ItemContent getItemContent(ItemStack stack){
        ItemContent itemContent = itemContentMap.get(stack.getDescriptionId());;
        if(itemContent != null)
            itemContent.setItemStack(stack);
        return itemContent;
    }

    public boolean hasItem(ItemStack itemStack){
        for(ItemContent content : itemContentMap.values()){
            if(itemStack.getDescriptionId().equals(content.itemID))
                return true;
        }
        return false;
    }

    public String getGroupName() {
        return groupName;
    }


}